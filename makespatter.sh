#!/bin/sh
#makespatter- Compiles spatter
#License: free software for noncommercial purposes- see LICENSE.txt for the full license

revision="$(date +%Y%m%d%H%M)"

#Fix permission errors
cd bin/spatter
chmod 644 cli.class
cd sequenceprocessing
chmod 644 *.class
cd ..
cd ..
cd ..

echo "Compiling the program..."
rm spatter.jar
ant

echo "Packaging the binary..."

#Nightly builds
mkdir spatter-${revision}
cp -r spatter.jar spatter-${revision}
zip -9 -u -r spatter-${revision}.zip spatter-${revision}
rm -rf spatter-${revision}

#Source bundle
echo "Packaging the source..."
mkdir spatter-${revision}-src
cp -r bin src .classpath .project build.xml makespatter.sh spatter-${revision}-src
zip -9 -u -r spatter-${revision}-src.zip spatter-${revision}-src
rm -rf spatter-${revision}-src
