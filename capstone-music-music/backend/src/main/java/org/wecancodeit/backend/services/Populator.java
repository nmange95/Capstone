package org.wecancodeit.backend.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.wecancodeit.backend.enums.ExperienceLevelEnum;
import org.wecancodeit.backend.enums.MusicTagsEnum;
import org.wecancodeit.backend.models.User;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Arrays;

@Component
public class Populator implements CommandLineRunner {

        private final UserService userService;

        public Populator(UserService userService) {
                this.userService = userService;
        }

        @Override
        public void run(String... args) throws Exception {
                String[] usernames = {
                                "CoffeeNerd42", "GizmoGadget", "SoccerFan1990", "Velociraptor", "GlazedDonut",
                                "PixelPirate", "ZenGarden", "RusticBarn", "Moonwalker", "FrostByte",
                                "SkaterDude", "TechieGal", "LostAstronaut", "CaramelTwist", "CosmicSurfer",
                                "BakerStreet221b", "LavaLampLove", "SunsetRider", "QuantumQuokka", "GamerGiraffe",
                                "NeonKnight", "MangoSalsa", "BinaryBard", "EvergreenElf", "StormChaser",
                                "BookishBadger", "ChilliPepper", "JazzHippo", "NoodleNinja", "PixelPanda",
                                "RetroRocket", "SushiSamurai", "TacoTuesday", "WaffleWizard", "ZenZebra",
                                "ArtsyAardvark", "BlizzardBear", "CupcakeConnoisseur", "DaringDragonfly", "EchoEagle",
                                "FunkyFrog", "GroovyGiraffe", "HikingHedgehog", "InkyImpala", "JollyJellyfish",
                                "KookyKangaroo", "LivelyLynx", "MysticMongoose", "NobleNarwhal", "OceanOctopus",
                                "User1", "User2", "User3"
                };
                String[] instruments = {
                                "Piano", "Guitar", "Violin", "Drums", "Flute",
                                "Saxophone", "Cello", "Clarinet", "Trumpet", "Harp",
                                "Accordion", "Trombone", "Bass Guitar", "Oboe", "Mandolin",
                                "Banjo", "Synthesizer", "Ukulele", "Xylophone", "French Horn"
                };
                String[] genres = {
                                "Rock", "Pop", "Jazz", "Classical", "Electronic",
                                "Country", "Blues", "Hip Hop", "Reggae", "Folk",
                                "Metal", "Punk", "Funk", "Soul", "R&B",
                                "Disco", "Gospel", "Opera", "Ska", "Grime"
                };
                String[] defaultProfilePics = {
                                "/media/pictures/default-pfp/Otter1.png",
                                "/media/pictures/default-pfp/Otter2.png",
                                "/media/pictures/default-pfp/Otter3.png",
                                "/media/pictures/default-pfp/Otter4.png",
                                "/media/pictures/default-pfp/Otter5.png",
                                "/media/pictures/default-pfp/Otter6.png",
                                "/media/pictures/default-pfp/Otter7.png",
                                "/media/pictures/default-pfp/Otter8.png",
                                "/media/pictures/default-pfp/Otter9.png"
                };
                User[] users = new User[usernames.length];

                for (int i = 0; i < usernames.length; i++) {
                        String username = usernames[i];
                        String password = "password";
                        String email = username + "@example.com";
                        String instrument = getRandomItemFromArray(instruments);
                        String genre = getRandomItemFromArray(genres);
                        String imageURL = getRandomItemFromArray(defaultProfilePics);
                        Set<MusicTagsEnum> musicTags = new HashSet<>();
                        for (int j = 0; j < 5; j++) {
                                musicTags.add(getRandomItemFromArray(MusicTagsEnum.values()));
                        }

                        ExperienceLevelEnum experienceLevel = getRandomItemFromArray(ExperienceLevelEnum.values());

                        User user = new User(username, password, email, instrument, genre, experienceLevel, imageURL);
                        users[i] = user;
                        user.setMusicTags(musicTags);
                        userService.createUser(user);
                }

                addRandomFriendsToUsers(users);

        }

        public void addRandomFriendsToUsers(@NonNull User[] users) {
                Random random = new Random();

                Set<String> excludedUsernames = new HashSet<>(Arrays.asList("User1", "User2", "User3"));

                for (User user : users) {
                        if (user == null) {
                                continue;
                        }
                        int numberOfFriends = 4 + random.nextInt(17);
                        Set<User> addedFriends = new HashSet<>();

                        while (addedFriends.size() < numberOfFriends) {
                                User friend = getRandomItemFromArray(users);

                                boolean isExcludedPair = excludedUsernames.contains(user.getUsername()) &&
                                                excludedUsernames.contains(friend.getUsername());

                                if (!friend.equals(user) && !user.getFriends().contains(friend) && !isExcludedPair) {
                                        addedFriends.add(friend);
                                        user.addFriend(friend);
                                }
                        }
                }
                updateAllUsers(users);
        }

        public void updateAllUsers(@NonNull User[] users) {
                for (User user : users) {
                        if (user == null) {
                                continue;
                        }
                        userService.updateUser(user);
                }
        }

        public static <T> T getRandomItemFromArray(T[] array) {
                Random random = new Random();
                return array[random.nextInt(array.length)];
        }
}
