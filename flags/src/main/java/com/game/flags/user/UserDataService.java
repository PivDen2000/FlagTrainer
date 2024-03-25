package com.game.flags.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.flags.flag.Flag;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@Service
public class UserDataService {

    private final Path templateFilePath = Path.of("src", "main", "resources", "data", "flags.json");
    private final Path dataDirectory = Path.of("src", "main", "resources", "data", "users");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createUserFlagsFile(String username) {
        try {
            Path userFilePath = getUserPath(username);

            // Створення директорії користувачів, якщо вона не існує
            Files.createDirectories(dataDirectory);

            // Копіювання файлу з заміною існуючого, якщо він вже є
            Files.copy(templateFilePath, userFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getUserPath(String username){
        return dataDirectory.resolve(username + ".json");
    }

    public List<Flag> loadUserFlags(String username) {
        try {
            File file = new File(String.valueOf(getUserPath(username)));
            if (!file.exists()) {
                createUserFlagsFile(username);
            }
            return objectMapper.readValue(file, new TypeReference<List<Flag>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateFlagFile(String username, Map<String, Flag> flags) {
        try {
            File file = new File(String.valueOf(getUserPath(username)));
            objectMapper.writeValue(file, flags.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}