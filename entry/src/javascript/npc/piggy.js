
var npcDefault = require('default/npcDefault.js').npc;

var npc = npcDefault;

npc.x = x ? x : 0.0;
npc.y = y ? y : 0.0;
npc.area = 0;
npc.experience = 100;
npc.aggroRange = 10;


npc.run = function (){
    var dt;
    var originalX = this.x, originalY = this.y;
    while(true){
        if(this.nextPathIndex != -1){
        }

    }
};