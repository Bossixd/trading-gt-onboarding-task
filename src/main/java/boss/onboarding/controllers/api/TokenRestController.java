package boss.onboarding.controllers.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import boss.onboarding.models.repository.TokenDataRepository;

@Controller
public class TokenRestController {
    private TokenDataRepository data = TokenDataRepository.getDataSingleton();
    
    @RequestMapping(value="/getAll", method=RequestMethod.GET)
    @ResponseBody
    public String getAll(@RequestParam(required = true) String token) {
        return "Token: " + token + ", Data: " + data.readAll(token).toString();
    }

    @RequestMapping(value="/get", method=RequestMethod.GET)
    @ResponseBody
    public String get(@RequestParam(required = true) String token, @RequestParam(required = true) int index) {
        return "Token: " + token + ", Data: " + data.read(token, index);
    }

    @RequestMapping(value="/getMax", method=RequestMethod.GET)
    @ResponseBody
    public String getMax(@RequestParam(required = true) String token) {
        return "Token: " + token + ", Data: " + data.getMax(token);
    }

    @RequestMapping(value="/getMin", method=RequestMethod.GET)
    @ResponseBody
    public String getMin(@RequestParam(required = true) String token) {
        return "Token: " + token + ", Data: " + data.getMin(token);
    }
}