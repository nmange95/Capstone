package org.wecancodeit.backend.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wecancodeit.backend.enums.MusicTagsEnum;

@RestController
@RequestMapping("/api/music-tags")
public class MusicTagsController {

    @GetMapping
    public ResponseEntity<List<String>> getMusicTags() {
        List<String> musicTags = Arrays.stream(MusicTagsEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(musicTags);
    }
}
