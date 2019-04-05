package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    private MessageRepo messageRepo;

    @Autowired
    public MainService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Iterable<Message> findAll() {
        return messageRepo.findAll();
    }

    public Iterable<Message> findByTag(String filter) {
        return messageRepo.findByTag(filter);
    }

    public void save(Message message) {
        messageRepo.save(message);
    }

    public void delete(Message message) {
        messageRepo.delete(message);
    }
}
