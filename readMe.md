# Site Clearning Simulation

This is a simple Java program that helps setting up a "Site" and then to "move" a Bulldozer as per simulated commands to the Bulldozer to move it within the Site boundaries. If any command causes the Bulldozer to move out of the "Site" then that simulation would exit.

This has been done in such a way that simulation can be continued with multiple rounds of command sequences.

Following are the commands understood:
```
(a)dvance <n>
(l)eft
(r)ight
```

## Usage

To run the program, checkout the code from github, then go the root of the project and on the command line, execute the below command to build it using gradle.

```
$ gradlew clean build --refresh-dependencies
```

Note: It is built using Java 8. (Please look at the java version set in build.gradle). if you have a different Java version like Java 11, please change the properties in build.gradle (eg: sourceCompatibility = 1.11, targetCompatibility = 1.11)

Now run the program using java runtime in your path (appropriate Java 8 / Java 11), for eg:

Note: Sitemap file can be specified as a commandline argument, and as well the Simulation commands also.
If they are given as command line, then the first argument would be Sitemap data file followed by Simulation commands input.

Method 1: Manually inputting data


```
$ java -jar build/libs/SiteClearingSimulation.jar

Enter sitemap details in the console. Enter blank line to finish input.
ootooooooo
oooooooToo
rrrooooToo
rrrroooooo
rrrrrtoooo



Welcome to the Aconex site clearing simulator. This is a map of the site:

o o t o o o o o o o
o o o o o o o T o o
r r r o o o o T o o
r r r r o o o o o o
r r r r r t o o o o


The bulldozer is currently located at the Northern edge of the site, immediately to the West of the site, and facing East.

Enter the commands for simulation, press q/quit to end:
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
a 5
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
r
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
a 2
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
r
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
a 4
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
q

The simulation has ended at your request. These are the commands you issued:
advance 5, turn right, advance 2, turn right, advance 4

The costs for this land clearing operation were:

Item                                                                                                       Quantity      Cost
communication overhead                                                                                            5         5
fuel usage                                                                                                       15        15
uncleared squares                                                                                                39       117
destruction of protected tree                                                                                     0         0
paint damage to bulldozer                                                                                         1         2
-------------------
Total                                                                                                                     139

```

Method 2: Run with the path of the sitemap data. (This sample has simulation exit example)

```
java -jar build/libs/SiteClearingSimulation.jar src/main/resources/data.txt


Welcome to the Aconex site clearing simulator. This is a map of the site:

o o t o o o o o o o
o o o o o o o T o o
r r r o o o o T o o
r r r r o o o o o o
r r r r r t o o o o


The bulldozer is currently located at the Northern edge of the site, immediately to the West of the site, and facing East.

Enter the commands for simulation, press q/quit to end:
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
a 4
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
r
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
a 5
(l)eft, (r)ight, (a)dvance <n>, (q)uit:
q

Aborting this simulation : Going out of bound of the site.. Simulation will stop.
```

Logging:
--------
Logging is implemented using logback. There is a config for this (logback.xml) under src/main/resources. This may be copied over to another location and further configured and the path might be provided to the runtime. (Bundled jar has the default log config set.)

```
$ java -Dlogback.configurationFile=<PATH>/logback.xml -jar build/libs/SiteClearingSimulation.jar
```


Dependency libraries' versions:
------------------------------
Gradle is used to manage the build and dependencies. The dependency library versions are configured in gradle.properties.

Lombok:
-------
Lombok library is used to minimize code and autogenerate where required. This will help in avoiding boilerplate code so as to concentrate only on main code.

Note: If Eclipse is being used as the IDE, then lombok plugin needs to be installed in Eclipse. Download from Maven the latest lombok jar and then click on the executable jar which would ask for location of Eclipse. Once that is setup, restart eclipse.

## Implementation -- Functionalities

'Direction' & MOVE:
------------------
Direction translates to a +/- unit in X-axis and Y-axis. (Ref: Direction.java)

eg:
		a. Direction = NORTH
				X-direction = 0;
				Y-direction = +1;
		b. Direction = WEST
				X-direction = -1;
				Y-direction = 0;			
			
		Once the above is set, it is very easy to do a Move.
			new X-Pos = current X-position + Direction.X-direction	
			new Y-Pos = current Y-position + Direction.Y-direction

##Assumptions
1) A Cartesian plane is assumed for the table and the direction is indicated with +1/-1/0 values for X-axis / Y-axis

    NORTH ==>  +Y-axis
    EAST  ==>  +X-axis
    SOUTH ==>  -Y-axis
    WEST  ==>  -X-axis

2) Input can be from the standard input or from file.

3) If file paths are given as input, then the first parameter of the program would correspond to the path of SiteMap data file, and the second param is the path of Simulation commands file. (Though it is possible to provide only the SiteMap data file path, in which case Simulation commands would be input from Console) 

Note: To denote End of file (input) in case of SiteMap data, leave a blank line followed by line feed/new line.

## Extensibility
If more commands need to be added, just add the implementation of corresponding command extending the Command interface, and provide this command in the Simulation class. (Add new case)
