package com.gai.psas.patent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.gai.psas.patent.model.patent.Patent;

import java.util.List;

public interface PatentRepository extends ElasticsearchRepository<Patent, String> {

    Page<Patent> findByAuthor(String author, Pageable pageable);

    List<Patent> findByTitle(String title);

}