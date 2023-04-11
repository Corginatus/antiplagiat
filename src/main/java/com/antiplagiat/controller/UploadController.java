package com.antiplagiat.controller;


import com.antiplagiat.calculation.KeywordExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Controller
@RequestMapping("/file")
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\37529\\antiplagiat\\src\\main\\files\\";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    //@RequestMapping(value = "/upload", method = RequestMethod.POST)
    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/file/uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

//            System.out.println("TEXT START");
//            List<String> loop_files = Files.readAllLines(path);
//            for (String loop_file : loop_files) {
//                System.out.println(loop_file);
//            }
//            System.out.println("TEXT END");

            System.out.println("---PROGRAM START---");
            File file_r = new File(UPLOADED_FOLDER + file.getOriginalFilename());

            try (FileReader fr = new FileReader(file_r))
            {
                char[] chars = new char[(int) file_r.length()];
                fr.read(chars);
                String text = new String(chars);
                System.out.println(text);
                Set<String> keywords = KeywordExtractor.extractKeywords(text);
                System.out.println("Keywords: " + keywords);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("---PROGRAM END---");
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/file/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @PostMapping("/uploadMulti")
    public String multiFileUpload(@RequestParam("files") MultipartFile[] files,
                                  RedirectAttributes redirectAttributes) {

        StringJoiner sj = new StringJoiner(" , ");

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            try {

                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);

                sj.add(file.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String uploadedFileName = sj.toString();
        if (StringUtils.isEmpty(uploadedFileName)) {
            redirectAttributes.addFlashAttribute("message",
                    "Please select a file to upload");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + uploadedFileName + "'");
        }

        return "redirect:/file/uploadStatus";

    }

    @GetMapping("/uploadMultiPage")
    public String uploadMultiPage() {
        return "uploadMulti";
    }

}