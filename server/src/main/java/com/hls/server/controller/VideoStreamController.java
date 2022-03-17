package com.hls.server.controller;

import com.hls.server.service.VideoStreamService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/video")
public class VideoStreamController implements WebFluxConfigurer {

    private final VideoStreamService videoStreamService;

    public VideoStreamController(VideoStreamService videoStreamService) {
        this.videoStreamService = videoStreamService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @GetMapping("*")
    public Mono<String> index() {
        System.out.println("Reached here");
        return Mono.just("This is index");
    }

    @GetMapping("/{adNumber}/{filename}")
    public Mono<ResponseEntity<byte[]>> manifest(ServerHttpResponse serverHttpResponse,
            @PathVariable("filename") String filename,
            @PathVariable("adNumber") String adNumber) {
        try {
            return Mono.just(videoStreamService.readFile(adNumber, filename));
        } catch (Exception e) {
            System.out.println("manifest :::: " + e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        }
    }

    @GetMapping("/encode/{adNumber}")
    public Mono<ResponseEntity<String>> encode(
            ServerHttpResponse serverHttpResponse,
            @PathVariable("adNumber") String adNumber) {
        try {
            return Mono.just(ResponseEntity.status(HttpStatus.OK).body(videoStreamService.encode(adNumber)));
        } catch (Exception e) {
            System.out.println("encode :::: " + e.getStackTrace());
            System.out.println("encode :::: " + e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
        }
    }
}
