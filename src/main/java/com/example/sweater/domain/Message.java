package com.example.sweater.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;

/**
 * Сообщение
 */
@Entity
public class Message {

    /**
     * Идентификатор сообщения
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    /**
     * Текст сообщения
     */
    @NotBlank(message = "Please fill the message")
    @Length(max = 255, message = "Message is too long")
    private String text;

    /**
     * Тег
     */
    @Length(max = 255, message = "Tag is too long")
    private String tag;

    /**
     * Автор сообщения
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    /**
     * Название файла
     */
    private String filename;

    /**
     * Количество загрузок файла
     */
    private int downloads = 0;

    public Message() {
    }

    public Message(final String text, final String tag, final User author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(final Integer downloads) {
        this.downloads = downloads;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public File getFile() {
        return new File(filename);
    }
}
