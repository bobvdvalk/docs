# MVP Phase
This phase we will try to deliver an [MVP](../02-minimum-viable-product.md).

## June 5th 2016
Today I started some work on the editor. I made a start on a tabbed
text view and the file manager. It still needs to be connected nicely
but the required public api is there.

The next thing I will do is take a look at a settings system so I can
store active projects and more settings.

## June 8th 2016
I implemented some UI operations like open file and open folder.
I also added the start of a editor toolbar. They still need icons and
the header levels still have to be implemented.

I implemented a basic settings system that will save all preferences
to the user's home folder. I wanted to use the java.util.prefs.Preferences
system first but oracle can't honestly expect all users to edit their
registry?
[Check out the issue](http://stackoverflow.com/questions/5354838/java-java-util-preferences-failing)
