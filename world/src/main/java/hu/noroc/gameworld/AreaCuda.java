package hu.noroc.gameworld;

import hu.noroc.gameworld.components.behaviour.Player;
import hu.noroc.gameworld.messaging.directional.AttackEvent;
import jcuda.Pointer;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

import java.awt.*;

public class AreaCuda extends Area {

    public AreaCuda(double sideLength, int mapWidth, World world) {
        super(sideLength, mapWidth, world);
    }

    @Override
    protected void applySpell(AttackEvent event) {
        final double xp = event.getBeing().getX();
        final double yp = event.getBeing().getY();

        final double xd = event.getX();
        final double yd = event.getY();
        final double r = event.getRadius();
        final double as = Math.toRadians(event.getAlpha());
        // Direction deg
        final double xdp = (xd - xp);
        final double ydp = (yd - yp);
        double ad = Math.atan(ydp / xdp);

        if (xdp < 0) {
            ad += Math.PI;
        } else if (ydp < 0) {
            ad += Math.PI * 2;
        }
        final double finalAd = ad;
        for (Player player : players) {
            // Target - Entity
            double xpt = player.getX() - xp;
            double ypt = player.getY() - yp;

            if (xpt == 0 && ypt == 0) {
                player.attacked(event.getEffect(), event.getBeing());
            }

            double absVecpt = Math.sqrt((xpt * xpt + ypt * ypt));

            if (absVecpt > r) {
                continue;
            }
            if (as == 180.0) {
                player.attacked(event.getEffect(), event.getBeing());
                continue;
            }
            double apt = Math.atan(ypt / xpt);
            if (xpt < 0) {
                apt += Math.PI;
            } else if (ypt < 0) {
                apt += Math.PI * 2;
            }

            if ((apt <= (finalAd + as)) && ((finalAd - as) <= apt)) {
                player.attacked(event.getEffect(), event.getBeing());
            }
        }

    }
}
