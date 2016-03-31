
exports.npc = new Object();

exports.npc.stats = {
    "health": 0,
    "mana": 0,
    "armor": 0,
    "magicResist": 0,
    "strength": 0,
    "stamina": 0,
    "intellect": 0,
    "spirit": 0
};

//Matrix
exports.npc.path = [];
exports.npc.nextPathIndex = 0;

exports.npc.setPath = function (path){
    this.path = path;
    if(this.path && this.path.length != 0)
        this.nextPathIndex = 0;
    else
        this.nextPathIndex = -1;
};

//TODO
exports.npc.isInside = function(x, y){
    this.aggroRange
}

exports.npc.getName = function(){
    return this.name;
};

exports.npc.getStats = function(){
    return this.stats.toJSON();
};
exports.npc.getX = function(){
    return this.x;
};
exports.npc.getY = function(){
    return this.y;
};
exports.npc.getArea = function(){
    return this.area;
};
exports.npc.getExperience = function(){
    return this.experience;
};
exports.npc.setArea = function(areaId){
    this.area = areaId;
};

