package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.task1.entity.Attachment;
import uz.pdp.task1.payload.ResponseAttachment;
import uz.pdp.task1.payload.Result;
import uz.pdp.task1.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    // CREATE
    @PostMapping("/upload")
    public Result upload(MultipartHttpServletRequest request) throws IOException {
        return attachmentService.upload(request);
    }

    // READ
    @GetMapping
    public List<Attachment> getAll() {
        return attachmentService.get();
    }

    // READ BY ID
    @GetMapping("/download/{id}")
    public ResponseAttachment getOne(@PathVariable Integer id, HttpServletResponse response) {
        return attachmentService.getById(id, response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Result edit(MultipartHttpServletRequest request, @PathVariable Integer id) throws IOException {
        return attachmentService.edit(request, id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return attachmentService.delete(id);
    }
}
