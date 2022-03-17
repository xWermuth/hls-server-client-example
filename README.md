# hls-server-client-example

## Running the project

### Build client

Navigate to ```app``` folder.
First install dependecies:
```npm install```.
Then run following command to build the client:
```npm run build:prod```

### Build server

Navigate to ```server``` folder. Run following command to start the server:
```mvn spring-boot:run```

### Start test site

Navigate to ```test-site``` folder. First install dependecies:
```npm install```.
Then run:
```npm run dev```

### Setup

First start by encoding a video. You can use your own one by adding it to the ```server/src/main/resources/video/<your_file.mp4>```.
Then call endpoint: ```http://localhost:8080/video/encode/<filename>``` where \<filename> is the file to be encoded.

Then navigate to ```http://localhost:3000/``` where the video will be displayed. To change the ad displayed change the src of the Iframes on line 25 or line 74.

## Encoding

To produce the stream a java wrapper of FFMPEG is used. The video are splitted into three streams of different resolution and bitrates. 
The current resolutions are:

- Orginial resolution

- Half the original resolution

- Qauter of the original resolution

To modify the resolution modify the ```w=<your_width>``` and ```h=<your_height>``` in the following file ```Encoder.java```

```java
"[0:v]split=3[v1][v2][v3]; [v1]copy[v1out]; [v2]scale=w=iw/2:h=ih/2[v2out ; [v3]scale=w=iw/4:h=ih/4[v3out]")
```

Depending on the video size, hardware and preset chosen for FFMPEG it can take several seconds to produce the streams.
