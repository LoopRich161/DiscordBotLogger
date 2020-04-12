package ru.looprich.discordlogger.module;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class LogsManager {

    public static void getLogFile(String date) {
        List<File> logFiles = new ArrayList<>();
        String logsPath = new File("logs").getAbsolutePath();
        File latestFileTemp = new File(logsPath + "/latest.log");
        File latestFile = new File("latest.log");
        try {
            if (!latestFile.exists())
                latestFile.createNewFile();
            Files.copy(latestFileTemp, latestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!date.equalsIgnoreCase("latest")) {
            for (File file : new File(logsPath).listFiles()) {
                if (file.getName().substring(0, 10).equals(date) && file.getName().endsWith(".gz")) {
                    logFiles.add(getLogInArchive(file));
                }
            }
        }
        if (logFiles.size() == 0 && !date.equalsIgnoreCase("latest")) {
            DiscordBot.sendServerResponse("Логов с такой датой не найдено!");
            return;
        }
        logFiles.add(latestFile);
        DiscordBot.sendLogs(logFiles);

        for (File file : logFiles)
            file.deleteOnExit();
    }

    private static File getLogInArchive(File archive) {
        byte[] buffer = new byte[1024];
        String outputFile = archive.getAbsolutePath().substring(0, archive.getAbsolutePath().length() - 7) + ".log";
        if (new File(outputFile).exists())
            return new File(outputFile);
        try {
            GZIPInputStream gzipInputStream =
                    new GZIPInputStream(new FileInputStream(archive.getAbsolutePath()));

            FileOutputStream fileOutputStream =
                    new FileOutputStream(outputFile);

            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }

            gzipInputStream.close();
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new File(outputFile);
    }


}
