package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.task1.entity.Attachment;
import uz.pdp.task1.entity.AttachmentContent;
import uz.pdp.task1.payload.ResponseAttachment;
import uz.pdp.task1.payload.Result;
import uz.pdp.task1.repository.AttachmentContentRepository;
import uz.pdp.task1.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public Result upload(MultipartHttpServletRequest request) throws IOException {

        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());

        Attachment attachment = new Attachment();
        if (file != null) {
            attachment.setName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAttachment(savedAttachment);
            attachmentContent.setBytes(file.getBytes());

            attachmentContentRepository.save(attachmentContent);
            return new Result("File Saved", true, savedAttachment.getId());
        }

        return new Result("Error", false);
    }

    public List<Attachment> get() {
        return attachmentRepository.findAll();
    }

    public ResponseAttachment getById(Integer id, HttpServletResponse response) {
        ResponseAttachment responseAttachment = new ResponseAttachment();

        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findAttachmentContentByAttachmentId(attachment.getId());
            if (optionalAttachmentContent.isPresent()) {
                AttachmentContent attachmentContent = optionalAttachmentContent.get();

                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + attachment.getName() + "\"");
                response.setContentType(attachment.getContentType());

                responseAttachment.setName(optionalAttachment.get().getName());
                responseAttachment.setSize(optionalAttachment.get().getSize());
                responseAttachment.setContentType(optionalAttachment.get().getContentType());
                responseAttachment.setMainContent(optionalAttachmentContent.get().getBytes());

                return responseAttachment;
            }
        }
        return null;
    }

    public Result edit(MultipartHttpServletRequest request, Integer id) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Iterator<String> fileNames = request.getFileNames();
            MultipartFile file = request.getFile(fileNames.next());

            if (file != null) {
                optionalAttachment.get().setName(file.getOriginalFilename());
                optionalAttachment.get().setSize(file.getSize());
                optionalAttachment.get().setContentType(file.getContentType());

                Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findAttachmentContentByAttachmentId(id);
                if (optionalAttachmentContent.isPresent()) {
                    optionalAttachmentContent.get().setAttachment(optionalAttachment.get());
                    optionalAttachmentContent.get().setBytes(file.getBytes());

                    attachmentContentRepository.save(optionalAttachmentContent.get());
                }

                return new Result("Edited", true);
            }

            return new Result("Not uploaded!", false);
        }
        return new Result("Not Found!", false);
    }

    public Result delete(Integer id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {

            attachmentRepository.deleteById(id);
            attachmentContentRepository.deleteAllByAttachment_Id(id);
            return new Result("Deleted", true);
        }
        return new Result("Not Found!", false);
    }

}
