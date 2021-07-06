package com.liyuanjinglyj.gifcard;

import com.liyuanjinglyj.gifcard.slice.ChoiceImageAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.io.FileDescriptor;

public class ChoiceImageAbility extends Ability {
    private static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x0001, "选择图片测试");
    private Button button;
    private Image image;
    private final int imgRequestCode=123;
    private long formId;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_choice_image);
        formId=intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, -1);
        requestPermissionsFromUser(new String[]{"ohos.permission.READ_USER_STORAGE"},imgRequestCode);
        this.button=(Button)findComponentById(ResourceTable.Id_ability_choice_image_button);
        this.image=(Image)findComponentById(ResourceTable.Id_ability_choice_image_image);
        this.button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation opt=new Intent.OperationBuilder().withAction("android.intent.action.GET_CONTENT").build();
                intent.setOperation(opt);
                intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                intent.setType("image/*");
                startAbilityForResult(intent, imgRequestCode);
            }
        });
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode==imgRequestCode)
        {
            HiLog.info(label,"选择图片getUriString:"+resultData.getUriString());
            //选择的Img对应的Uri
            String chooseImgUri=resultData.getUriString();
            HiLog.info(label,"选择图片getScheme:"+chooseImgUri.substring(chooseImgUri.lastIndexOf('/')));
            //定义数据能力帮助对象
            DataAbilityHelper helper=DataAbilityHelper.creator(getContext());
            //定义图片来源对象
            ImageSource imageSource = null;
            //获取选择的Img对应的Id
            String chooseImgId=null;
            //如果是选择文件则getUriString结果为content://com.android.providers.media.documents/document/image%3A30，其中%3A是":"的URL编码结果，后面的数字就是image对应的Id
            //如果选择的是图库则getUriString结果为content://media/external/images/media/30，最后就是image对应的Id
            //这里需要判断是选择了文件还是图库
            if(chooseImgUri.lastIndexOf("%3A")!=-1){
                chooseImgId = chooseImgUri.substring(chooseImgUri.lastIndexOf("%3A")+3);
            }
            else {
                chooseImgId = chooseImgUri.substring(chooseImgUri.lastIndexOf('/')+1);
            }
            //获取图片对应的uri，由于获取到的前缀是content，我们替换成对应的dataability前缀
            Uri uri=Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,chooseImgId);
            HiLog.error(label,"选择图片dataability路径:"+uri.toString());
            ZSONObject zsonObject=new ZSONObject();
            zsonObject.put("imageUrl",uri.toString());
            FormBindingData bindingData = new FormBindingData(zsonObject);
            try {
                //读取图片
                updateForm(formId,bindingData);
                FileDescriptor fd = helper.openFile(uri, "r");
                imageSource = ImageSource.create(fd, null);
                //创建位图
                PixelMap pixelMap = imageSource.createPixelmap(null);
                //设置图片控件对应的位图
                image.setPixelMap(pixelMap);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (imageSource != null) {
                    imageSource.release();
                }
            }
        }
    }
}
