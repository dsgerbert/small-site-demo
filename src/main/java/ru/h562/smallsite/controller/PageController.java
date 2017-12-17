package ru.h562.smallsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.h562.smallsite.SmallSiteConst;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PageController {
    @Autowired
    private SmallSiteConst smallSiteConst;

    @RequestMapping(value = "/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/clean_general")
    public String clearnGeneral(Model model) {
        model.addAttribute(SmallSiteConst.ORDER_TYPE, "1");

        return "clean_general";
    }

    @RequestMapping(value = "/clean_repair")
    public String clearnRepair(Model model) {
        model.addAttribute(SmallSiteConst.ORDER_TYPE, "2");

        return "clean_repair";
    }

    @RequestMapping(value = "/clean_support")
    public String clearnSupport() {
        return "clean_support";
    }

    @RequestMapping(value = "/clean_windows")
    public String clearnWindows(Model model) {
        model.addAttribute(SmallSiteConst.ORDER_TYPE, "3");

        return "clean_windows";
    }

    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/payment")
    public String payment(Model model) {
        LocalDateTime date = LocalDateTime.now();
        String paymentNum = date.format(DateTimeFormatter.ofPattern("yyyy-D-HHmmss"));

        model.addAttribute("paymentNum", paymentNum);
        model.addAttribute("yandexSuccess", smallSiteConst.YANDEX_SUCCESS);
        model.addAttribute("yandexWallet", smallSiteConst.YANDEX_WALLET);

        return "payment";
    }
}
