var Thread = java.lang.Thread

var npc = new Object();

npc.x = 1.0;
npc.y = -1.0;
npc.area = 0;

npc.run = function (){
    while(this.area != 1) {
        print('Position: x ' + this.x + ', y ' + this.y);
        print('Area: ' + this.area);
        Thread.sleep(500);
    }
};

npc.move = function(x1, y1){
    this.x += x1;
    this.y += y1;
};
npc.stop = function(){
    this.area = 1;
};
