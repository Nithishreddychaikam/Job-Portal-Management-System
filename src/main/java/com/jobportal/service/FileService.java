package com.jobportal.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public String uploadResume(MultipartFile file) throws IOException {
        ObjectId objectId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );
        return objectId.toString();
    }

    public GridFSFile getResumeFile(String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }
    
    public ByteArrayResource downloadResumeResource(String id) throws IOException {
        GridFSFile file = getResumeFile(id);
        if(file != null) {
            return new ByteArrayResource(gridFsOperations.getResource(file).getContentAsByteArray());
        }
        return null;
    }
}
