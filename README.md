# README #

### What is this? ###

*manning* is a text based live ticker for NFL games. All live data is collected from NFL's official site where it is publicly available in JSON format. Sources are

* www.nfl.com/liveupdate/scorestrip/ss.json (for week overview)
* www.nfl.com/liveupdate/game-center/<ID>/<ID>_gtd.json (for single games where <ID> matches the JSON attribute "eid" found at the link above)

### System requirements ###

In order to use manning, you'll need Java 8 installed. As Java 8 is available for almost any operating system, this shouldn't be a problem in most cases. In addition, manning is designed and developed to run in UNIX terminals. Due to the lack of color support, the single game view is not guaranteed to work (in other words: will definitely not work) in Windows terminals. However, it may be possible to run the overview, since it does not use color codes.  

### How to set up ###

You can choose between these ways:

* Clone & Compile
    1. Clone this repository using the following command:
    ```git clone https://rngcntr@bitbucket.org/rngcntr/manning.git```
    2. Compile the source files. Simply run ```make jar``` inside your manning folder.
* Download zip
    1. Download the latest .zip file from the Downloads section
    2. Extract it and you're done

### Basic usage ###

There are two different views provided by manning which cover an overview of all games as well as detailed information of a single game.

In order to start the overview, all you need to do is run ```java -jar manning.jar``` inside the manning folder. The only further option there is pressing 'q' to exit the program.

The more complex perspective is designed for following single games. Therefore, start manning by running ```java -jar manning.jar <TEAM>``` where <TEAM> stands for the two or three letter abbreviation of the team you're interested in (if you don't know these abbrevations, head over to overview). Here you can use 'a' and 'd' to navigate through the game's drives and 'w' and 's' to navigate through a drive's plays. No matter how far you have scrolled, typing '0' will always bring you back to the latest available play.

### I found errors! ###

This piece of software is by far not error-free. Feel free to report issues and I'll see what I can do ;)