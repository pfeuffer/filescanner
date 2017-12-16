# Motion Control

This is a small web service to control motion (https://motion-project.github.io/),
"a highly configurable program that monitors video signals from many types of
cameras".

Goal is to start and stop motion process and to send alert mails whenever a motion is
detected (that is, a image file is stored by motion).

## Tech stuff

The service is implemented using Spring Boot (http://start.spring.io/). Therefor you
can build it simply using maven:

```mvn clean package```

This should create a jar in the ```target``` folder named ```motioncontrol-0.2-SNAPSHOT.jar```
(version may change). You can start the web service directly:

```java -jar filewatch-0.2-SNAPSHOT.jar```

However, you want to configure things. Configuration is done either using java switches or
program switches (see the example below for clarification).

The most important settings are listed in the table below:

| usecase | switch | program switch | default | example |
|-----|-----|---|---|---|
| web server port | server.port | ☐ | 8080 | -Dserver.port=8888 |
| password | security.user.password | ☐ | printed in standard out during server startup | -Dsecurity.user.password=pwd |
| directory where motion stores its images | directory | ☑ | \<temp_dir\>/motioncontrol | --directory=~/motion |
| target mail address for alert mails | mail.recipient | ☑ | yourusername@example.com | --mail.recipient=me@example.com |
| sender mail address for alert mails | mail.sender | ☑ | sender@example.com | --mail.sender=motion@example.com |
| mail host | mail.host | ☑ | localhost | --mail.host=mymailhost.local |
| command used to start motion | startup.command | ☑ | motion | --startup.command=~/motion.sh |
| interval how often the directory will be scanned in ms | scanIntervalMs | ☑ | 30000 (every half minute) | --scanIntervalMs=600000 |
| time window to group files | groupTime | ☑ | 300000 (5 minutes) | --groupTime=900000 |
| specify accepted image types | acceptedTypes | ☑ | .jpg,.tiff | --acceptedTypes=.jpg |

The principle command looks like this:

```
java 
   -D<java_switch_1>=<value_1> 
   -D<java_switch_2>=<value_2> 
   -jar filewatch-0.2-SNAPSHOT.jar
   --<program_switch_1>=<value_3>
   --<program_switch_2>=<value_4>
```

(Mind the D's for the java switches!)

A typical command to start the service can look like this:

```
java 
   -Dsecurity.user.password=pwd 
   -jar filewatch-0.2-SNAPSHOT.jar
   --directory=/my/path/to/motion
   --mail.recipient=me@example.com
   --mail.sender=motion@example.com
```

## Functionality

Once the service is up, it will monitor the specified directory for new files (eg. new images
captured by motion). If new files are found, a mail will be sent out.

Furthermore you can use this service to start and stop the motion process (mind, that motion
has to be configured to *not* run in daemon mode!).

To start motion, you can use curl:

```curl -u user:pwd -X POST http://localhost:8080/start```

To stop it:

```curl -u user:pwd -X POST http://localhost:8080/stop```

You can get the status calling:

```curl -u user:pwd http://localhost:8080/status```

## Further development

This is the plan for upcoming development:

1. Include links in alert mails to see the images captured by motion.
1. Create a little web frontend to get rid of ```curl```.
1. Develop a simple alarm app for Android.
