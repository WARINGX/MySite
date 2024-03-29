package cn.lhd.controller.admin;

import cn.lhd.constant.LogActions;
import cn.lhd.constant.WebConst;
import cn.lhd.controller.BaseController;
import cn.lhd.model.OptionsDomain;
import cn.lhd.service.log.LogService;
import cn.lhd.service.option.OptionService;
import cn.lhd.utils.APIResponse;
import cn.lhd.utils.GsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Donghua.Chen on 2018/4/30.
 */
@Api("系统设置")
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController {

    @Autowired
    private OptionService optionService;

    @Autowired
    private LogService logService;


    @ApiOperation("进入设置页")
    @GetMapping(value = "")
    public String setting(HttpServletRequest request){
        List<OptionsDomain> optionsList = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        optionsList.forEach((option) -> {
            options.put(option.getName(), option.getValue());
        });
        request.setAttribute("options", options);
        return "admin/setting";
    }


    @ApiOperation("保存系统设置")
    @PostMapping(value = "")
    @ResponseBody
    public APIResponse saveSetting(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> querys = new HashMap<>();
            parameterMap.forEach((key, value) -> {
                querys.put(key, join(value));
            });
            optionService.saveOptions(querys);
            WebConst.initConfig = querys;

            logService.addLog(LogActions.SYS_SETTING.getAction(), GsonUtils.toJsonString(querys), request.getRemoteAddr(), this.getUid(request));
            return APIResponse.success();
        } catch (Exception e) {
            String msg = "保存设置失败";
            return APIResponse.fail(e.getMessage());
        }
    }







}
