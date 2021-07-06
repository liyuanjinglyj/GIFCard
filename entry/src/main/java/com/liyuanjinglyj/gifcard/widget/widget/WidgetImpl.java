package com.liyuanjinglyj.gifcard.widget.widget;

import com.liyuanjinglyj.gifcard.common.LYJConstant;
import com.liyuanjinglyj.gifcard.widget.controller.FormController;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

/**
 * Form controller implementation.
 */
public class WidgetImpl extends FormController {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetImpl.class.getName());

    private static final int DEFAULT_DIMENSION_2X2 = 2;

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        HiLog.info(TAG, "bind form data for a new service widget.");
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (dimension == DEFAULT_DIMENSION_2X2) {
            ZSONObject formData = getData();
            providerFormInfo.setJsBindingData(new FormBindingData(formData));
        }
        return providerFormInfo;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        HiLog.info(TAG, "update form data: formId" + formId);
    }


    @Override
    public void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "handle trigger form event.");
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }

    private ZSONObject getData() {
        ZSONObject newFormData = new ZSONObject();
        newFormData.put("imageUrl", LYJConstant.imageFilePath[0]);
        return newFormData;
    }
}
