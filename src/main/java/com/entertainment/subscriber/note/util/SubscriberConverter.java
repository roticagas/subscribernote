package com.entertainment.subscriber.note.util;

import com.entertainment.subscriber.note.dao.SubscriberDao;
import com.entertainment.subscriber.note.model.SubscriberModel;

import java.time.LocalDateTime;

public class SubscriberConverter {
    public static SubscriberModel convertToSubscriberModel(SubscriberDao subscriberDao) {
        SubscriberModel subscriberModel = new SubscriberModel();
        if (subscriberDao != null) {
            if (subscriberDao.getName() != null) {
                subscriberModel.setName(subscriberDao.getName());
            }
            if (subscriberDao.getTitle() != null) {
                subscriberModel.setTitle(subscriberDao.getTitle());
            }
            if (subscriberDao.getStatus() != null) {
                subscriberModel.setStatus(subscriberDao.getStatus());
            }
        }
        subscriberModel.setRecurringAt(LocalDateTime.now());
        subscriberModel.setCreatedAt(LocalDateTime.now());
        subscriberModel.setModifiedAt(LocalDateTime.now());
        return subscriberModel;
    }
}
