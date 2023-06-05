package com.antiplagiat.controller;


import com.antiplagiat.calculation.KeywordExtractor;
import com.antiplagiat.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.antiplagiat.calculation.Test.fun;

@Controller
@RequestMapping("/file")
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\37529\\antiplagiat\\src\\main\\files\\";

    @PostMapping("/text")
    public ModelAndView del_job_owner(ModelAndView model,
                                @RequestParam(name = "text") String text) {
        File folder = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files");
        File[] listOfFiles = folder.listFiles();
        String str = null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(text)) {
                    str = text;
                    break;
                }
            }
        }
        File file = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files\\" + str);
        String textf = null;
        try (FileReader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            textf = new String(chars);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addObject("text", textf);
        model.addObject("name", str);
        model.setViewName("text");
        return model;
    }

    @GetMapping("/texts")
    public ModelAndView texts(ModelAndView model) {
        File folder = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files");
        File[] listOfFiles = folder.listFiles();

        List<String> texts = new ArrayList<String>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
//                System.out.println(file.getName());
                texts.add(file.getName());
            }
        }
        model.addObject("texts", texts);
        model.setViewName("texts");
        return model;
    }

    @PostMapping("/text_final")
    public ModelAndView texts_compare(ModelAndView model, @RequestParam(name = "name1") String name1,
                                      @RequestParam(name = "name2") String name2) {
        System.out.println("HERE");
//        String result = null;
        List<String> result = fun(name1, name2);
        model.addObject("result", result);
        model.setViewName("text_final");
        return model;
    }
    @PostMapping("/texts_compare")
    public ModelAndView texts2(ModelAndView model, @RequestParam(name = "name") String name) {
        File folder = new File("C:\\Users\\37529\\antiplagiat\\src\\main\\files");
        File[] listOfFiles = folder.listFiles();

        List<String> texts = new ArrayList<String>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
//                System.out.println(file.getName());
                if (!name.equals(file.getName()))
                    texts.add(file.getName());
            }
        }
        model.addObject("texts", texts);
        model.addObject("name", name);
        model.setViewName("texts_compare");
        return model;
    }


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