#!/bin/bash

echo "Pack game.."
mkdir game
mv desktop/build/libs/desktop-1.0.jar desktop/build/libs/pragma.jar

echo "Deploying to itch.io.."
wget http://dl.itch.ovh/butler/linux-amd64/head/butler
chmod +x butler
touch butler_creds
echo -n $ITCH_API_KEY > butler_creds

# Upload game
./butler push pragma.jar bitbrain/pragma:windows-linux-mac -i butler_creds

# Cleanup
echo "Cleanup.."
./butler logout -i butler_creds --assume-yes
rm -rf pragma.jar
rm butler

echo "Done."