package com.example.sweater.service.file;

import com.example.sweater.domain.Message;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Сервис для работы с файлами
 */
@Service
public class FileManager {

    @Autowired
    private Logger log;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Сохранить файл на сервер
     *
     * @param message сообщение, к которому прикреплен файл
     * @param file файл
     */
    public void saveFile(final Message message, final MultipartFile file) {

        try {
            if (file != null && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {

                log.info("Saving file named {}", file.getOriginalFilename());

                final File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                final String uuidFile = UUID.randomUUID().toString();
                final String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));

                message.setFilename(resultFilename);

                log.info("Done");
            }
        } catch (IOException e) {
            log.error("Error occurred whilst saving file named {}. Error: {}", message.getFilename(), e.getMessage());
        }
    }

    /**
     * Удалить файл на сервере
     *
     * @param message сообщение, к которому прикреплен файл
     */
    public void deleteFile(final Message message) {

        log.info("Deleting file named {}", message.getFilename());

        if (!StringUtils.isEmpty(message.getFilename())) {
            final File fileToDelete = new File(uploadPath + "/" + message.getFilename());
            fileToDelete.delete();
        } else {
            log.info("There is no file to delete from message of {} with tag {}",
                    message.getAuthorName(), message.getTag());
        }
        message.setFilename(null);

        log.info("Done");
    }

    /**
     * Загрузить файл с сервера
     *
     * @param message сообщение, к которому прикреплен файл
     * @param response ответ сервера, содержащий файл
     */
    public void downLoadFile(final Message message,
                             final HttpServletResponse response) {

        try {
            log.info("Downloading file named {} from server", message.getFilename());

            final File fileToDownload = new File(uploadPath + "/" + message.getFilename());
            final InputStream inputStream = new FileInputStream(fileToDownload);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

            log.info("Done");
        } catch (IOException e) {
            log.error("Error occurred whilst downloading file named {} from server. Error: {}", message.getFilename(), e.getMessage());
        }
    }
}
