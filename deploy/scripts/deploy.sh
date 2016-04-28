#!/bin/sh
if [ -d "/config/noroc" ]; then
    rm -r /config/noroc
fi
if [ ! -d "/config" ]; then
    mkdir /config
fi
mkdir /config/noroc
mkdir /config/noroc/npc

cd ../../

cp -r game/ /config/noroc/

gradle fullBuild
cp build/libs/GameWorld-1.0.jar /opt/Noroc/GameWorld-1.0.jar
chmod 777 /opt/Noroc/GameWorld-1.0.jar
