package hu.noroc.gameworld;

import hu.noroc.common.data.model.character.CharacterClass;
import hu.noroc.common.data.model.character.CharacterStat;
import hu.noroc.common.data.model.character.PlayerCharacter;
import hu.noroc.common.data.model.spell.SpellEffect;
import hu.noroc.gameworld.components.behaviour.Being;
import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.components.behaviour.spell.DamageLogic;
import hu.noroc.gameworld.messaging.EntityActivityType;
import hu.noroc.gameworld.messaging.Event;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import hu.noroc.gameworld.messaging.directional.DirectionalEvent;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jcuda.driver.JCudaDriver.*;
import static jcuda.driver.JCudaDriver.cuInit;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AreaTest {

    private List<Player> beings = new ArrayList<>();
    private Area area;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void generateData() throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter("test_beings.json"))) {
            out.write("[");
            for (int i = 0; i < 5000; i++) {
                out.write("{");
                out.write("\"x\":");
                out.write(String.format("%.4f", Math.random() * 100.0));
                out.write(",\"y\":");
                out.write(String.format("%.4f", Math.random() * 100.0));
                out.write("},");
            }
            out.write("]");
            out.flush();
        }
    }


    @Test
    public void speedTest() throws InterruptedException, IOException {

        List<FakeBeing> config = (new ObjectMapper()).readValue(
                Paths.get("test_beings.json").toFile(),
                new TypeReference<List<FakeBeing>>() {
                }
        );
        beings = config.stream().map(fakeBeing -> {
            CharacterStat stats = new CharacterStat();
            CharacterClass characterClass = new CharacterClass();
            characterClass.setStat(stats);
            Player being = new FakePlayer();
            being.setCharacter(new PlayerCharacter("Name", "doesn't", "matter"));
            being.setCharacterClass(characterClass);
            being.setStats(stats);
            being.setX(fakeBeing.x);
            being.setY(fakeBeing.y);
            return being;
        }).collect(Collectors.toList());


        World world = mock(World.class);
        area = new AreaCuda(100.0, 1, world);
        beings.forEach(area::newPlayer);

        Being being = mock(Being.class);
        when(being.getX()).thenReturn(50.0);
        when(being.getY()).thenReturn(50.0);

        AttackEvent event = new AttackEvent(DirectionalEvent.DirectionalType.CAST);
        event.setBeing(being);
        event.setX(51.0);
        event.setY(51.0);
        CharacterStat characterStat = new CharacterStat();
        characterStat.health = 100;
        event.setEffect(new DamageLogic(new SpellEffect(
                characterStat,
                SpellEffect.SpellType.DAMAGE,
                SpellEffect.DamageType.PHYSICAL,
                false,
                0,
                0
        ), "Spell", "SpellId", "Character"));
        event.setActivity(EntityActivityType.ATTACK);
        for (int i = 0; i < 10; i++) {
            Instant start = Instant.now();
            for (int j = 0; j < 10000; j++) {
                area.applySpell(event);
            }
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end).toMillis());
        }
    }

    @Test
    public void testCuda(){

        int count = 10000;
//        float[] sums = new float[count];

        // Enable exceptions and omit all subsequent error checks
        setExceptionsEnabled(true);

        // Initialize the driver and create a context for the first device.
        cuInit(0);
        CUdevice device = new CUdevice();
        cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        cuCtxCreate(context, 0, device);

        CUmodule module = new CUmodule();
        cuModuleLoad(module, "src/test/resources/hitbox.ptx");
        CUfunction function = new CUfunction();
        cuModuleGetFunction(function, module, "add");


        // Allocate the device input data, and copy the
        // host input data to the device
//        CUdeviceptr deviceInputA = new CUdeviceptr();
//        cuMemAlloc(deviceInputA, count * Sizeof.FLOAT);
//        cuMemcpyHtoD(deviceInputA, Pointer.to(hostInputA),
//                     numElements * Sizeof.FLOAT);
//        CUdeviceptr deviceInputB = new CUdeviceptr();
//        cuMemAlloc(deviceInputB, numElements * Sizeof.FLOAT);
//        cuMemcpyHtoD(deviceInputB, Pointer.to(hostInputB),
//                     numElements * Sizeof.FLOAT);

        // Allocate device output memory
        CUdeviceptr deviceOutput = new CUdeviceptr();
        cuMemAlloc(deviceOutput, count * Sizeof.FLOAT);


        Pointer arguments = Pointer.to(
                Pointer.to(new int[]{count}),
                Pointer.to(deviceOutput)
        );
        int blockSizeX = 256;
        int gridSizeX = (int)Math.ceil((double)count / blockSizeX);
        cuLaunchKernel(function,
                       gridSizeX,  1, 1,      // Grid dimension
                       blockSizeX, 1, 1,      // Block dimension
                       0, null,               // Shared memory size and stream
                       arguments, null // Kernel- and extra parameters
        );
        cuCtxSynchronize();


        // Allocate host output memory and copy the device output
        // to the host.
        float hostOutput[] = new float[count];
        cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput,
                     count * Sizeof.FLOAT);

        // Verify the result
        boolean passed = true;
        for(int i = 0; i < count; i++) {
            float expected = i;
            if (Math.abs(hostOutput[i] - expected) > 1e-5) {
                System.out.println(
                        "At index "+i+ " found "+hostOutput[i]+
                                " but expected "+expected);
                passed = false;
                break;
            }
        }
        System.out.println("Test "+(passed?"PASSED":"FAILED"));

        // Clean up.
        cuMemFree(deviceOutput);
    }

    public static class FakePlayer extends Player {
        @Override
        public void newEvent(Event message) {
        }
    }

    public static class FakeBeing {
        private double x, y;

        public FakeBeing() {
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }
    }
}
