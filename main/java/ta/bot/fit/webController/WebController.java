/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.bot.fit.webController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Joey
 */
@Controller
public class WebController {

    static Logger log = Logger.getLogger(WebController.class.getName());

    /*********************************
     * Spring Beans Inject
     *********************************/

    /****************************
     *        Web API          *
     ****************************/

    @RequestMapping(value = "/main")
    public ModelAndView main() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("main");
        return mv;
    }


}
