package com.example.sweater.service.file;

import com.example.sweater.domain.Message;
import org.apache.commons.io.IOUtils;
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

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Сохранить файл на сервер
     *
     * @param message сообщение, к которому прикреплен файл
     * @param file файл
     * @throws IOException
     */
    public void saveFile(Message message, MultipartFile file) throws IOException {

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    /**
     * Удалить файл на сервере
     *
     * @param message сообщение, к которому прикреплен файл
     */
    public void deleteFile(Message message) {

        File fileToDelete = new File(uploadPath + "/" + message.getFilename());
        if (!StringUtils.isEmpty(message.getFilename())) {
            fileToDelete.delete();
        }
        message.setFilename(null);
    }

    /**
     * Загрузить файл с сервера
     *
     * @param message сообщение, к которому прикреплен файл
     * @param response ответ сервера, содержащий файл
     * @throws Exception
     */
    public void downLoadFile(Message message,
                             HttpServletResponse response) throws Exception {

        File fileToDownload = new File(uploadPath + "/" + message.getFilename());
        InputStream inputStream = new FileInputStream(fileToDownload);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        inputStream.close();
    }
}
