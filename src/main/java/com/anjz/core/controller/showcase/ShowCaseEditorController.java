package com.anjz.core.controller.showcase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseCRUDController;
import com.anjz.core.model.ShowcaseEditor;

/**
 * 编辑器列表
 * @author ding.shuai
 * @date 2016年9月23日下午11:36:25
 */
@Controller
@RequestMapping("/showcase/editor")
public class ShowCaseEditorController extends BaseCRUDController<ShowcaseEditor, String>{

	public ShowCaseEditorController() {
        setResourceIdentity("showcase:editor");
    }
}
