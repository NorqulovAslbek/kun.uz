package com.example.service;

import com.example.entity.NewArticleTypeEntity;
import com.example.repository.NewArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class NewArticleTypeService {
    @Autowired
    private NewArticleTypeRepository newArticleTypeRepository;


    public void create(String id, List<Integer> articleType) {
        for (Integer art : articleType) {
            create(id, art);
        }
    }

    public void create(String id, Integer articleType) {
        NewArticleTypeEntity entity = new NewArticleTypeEntity();
        entity.setArticleId(id);
        entity.setArticleTypeId(articleType);
        newArticleTypeRepository.save(entity);
    }

    public void merge(String articleId, List<Integer> newList) {
        //oldin [7,8,1,2,3]
        //keyin [5]
        List<NewArticleTypeEntity> articleList = newArticleTypeRepository.findArticleId(articleId);
        for (NewArticleTypeEntity newArticleTypeEntity : articleList) {
            int count = 0;
            for (Integer natId : newList) {
                if (newArticleTypeEntity.getArticleTypeId().equals(natId)) {
                    count++;
                }
            }
            if (count == 0) {
                newArticleTypeRepository.deleteEn(newArticleTypeEntity.getArticleId(), newArticleTypeEntity.getArticleTypeId());
            }else {
                newArticleTypeRepository.updateDate(LocalDateTime.now(),newArticleTypeEntity.getArticleId(), newArticleTypeEntity.getArticleTypeId());
                newList.remove(newArticleTypeEntity.getArticleTypeId());
            }

        }
        for (Integer integer : newList) {
            NewArticleTypeEntity entity = new NewArticleTypeEntity();
            entity.setArticleId(articleId);
            entity.setArticleTypeId(integer);
            newArticleTypeRepository.save(entity);
        }
    }


}
