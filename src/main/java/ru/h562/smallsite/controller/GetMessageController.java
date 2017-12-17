package ru.h562.smallsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.h562.smallsite.SmallSiteConst;
import ru.h562.smallsite.service.MessageBusService;

@Controller
public class GetMessageController {

    @Autowired
    MessageBusService bus;

    private static final String SUCCESS_TYPE = "alert-success";

    private static final String SUCCESS_TITLE_PAYMENT = "Ваш платеж получен";
    private static final String PAYMENT_ANSWER_MESSAGE = "Благодарим за обращение к нам";

    private void putOutInfo(RedirectAttributes attributes, String type, String title, String message) {
        attributes.addFlashAttribute("outMessageType", type);
        attributes.addFlashAttribute("outMessageTitle", title);
        attributes.addFlashAttribute("outMessage", message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getMessageForm() {
        return "message";
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public String getChatForm() {
        return "email";
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String getOrderForm(@RequestParam(value = SmallSiteConst.ORDER_TYPE, required = false, defaultValue = "") String orderType,
                               Model model) {
        if (!orderType.isEmpty()) {
            model.addAttribute(SmallSiteConst.ORDER_TYPE, orderType);
        } else {
            model.addAttribute(SmallSiteConst.ORDER_TYPE, "0");
        }

        return "order";
    }

    @RequestMapping(value = "/phone", method = RequestMethod.GET)
    public String getPhoneForm(@RequestParam(value = SmallSiteConst.ORDER_TYPE, required = false, defaultValue = "") String orderType,
                               Model model) {
        if (!orderType.isEmpty()) {
            model.addAttribute(SmallSiteConst.ORDER_TYPE, orderType);
        } else {
            model.addAttribute(SmallSiteConst.ORDER_TYPE, "0");
        }

        return "phone";
    }

    @RequestMapping(value = "/success_payment", method = RequestMethod.GET)
    public String successPayment(RedirectAttributes attributes) {
        putOutInfo(attributes, SUCCESS_TYPE, SUCCESS_TITLE_PAYMENT, PAYMENT_ANSWER_MESSAGE);

        return "redirect:/";
    }
}
