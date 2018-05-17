#!/bin/bash

echo "Pack game.."

mv desktop/build/libs/desktop-1.0.jar desktop/build/libs/pragma.jar

echo "Deploying to itch.io.."
wget http://dl.itch.ovh/butler/linux-amd64/head/butler

chmod +x butler

# Upload game
./butler push desktop/build/libs/pragma.jar bitbrain/pragma:windows-linux-mac

# Cleanup
echo "Cleanup.."
./butler logout -i butler_creds --assume-yes
rm -rf desktop/build
rm butler

echo "Done."