package org.example;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.example.utils.URLUtils;
import org.example.utils.Urlconvert;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public final class Plugin extends JavaPlugin {
    public static final Plugin INSTANCE = new Plugin();
    //要读取的文件夹
    String fileH = "D:\\images\\H\\";
    //定义数组存取文件名
    String[] fileNameH;
    //索引
    int numH = 0;

    private Plugin() {
        super(new JvmPluginDescriptionBuilder("org.example.plugin", "1.0-SNAPSHOT").build());
    }

    //onLoad方法在插件运行时会自动执行，且只执行一次，可以用作读取文件、配置等。
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        fileNameH = getFileName(fileH);
    }

    @Override
    public void onEnable() {
        //个人提醒
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event -> {
            String mesJson = event.getMessage().contentToString();// 获取消息内容，并转换成接近官方格式的字符串
            Long qqNum = event.getSender().getId(); // 获取该信息发送者的QQ号，返回类型为Long
            Long Num = event.getSubject().getId(); // 获取消息来源，好友消息则是好友QQ号，群组消息则是群号，返回类型为Long

            String s = event.getMessage().serializeToMiraiCode();
            String substring = s.substring(13, s.length() - 1);
//            event.getSender().sendMessage(substring);

//            event.getSender().sendMessage(new PlainText("你发的图片为：").plus(Image.fromId(substring))); // 一个纯文本加一个图片

//            event.getSender().sendMessage("理你一下");
        });

        //群聊提醒
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {

            String mesJson = event.getMessage().contentToString();// 获取消息内容，并转换成接近官方格式的字符串
            Long qqNum = event.getSender().getId(); // 获取该信息发送者的QQ号，返回类型为Long
            Long Num = event.getSubject().getId(); // 获取消息来源，好友消息则是好友QQ号，群组消息则是群号，返回类型为Long

            //要检测的群号
            if (Num == 881426228) {
                //相关代码都在这里写
                //如果收到信息为“kkp”等关键词则发色图
                if (mesJson.contains("kkp") || mesJson.contains("涩图") || mesJson.contains("色图")) {
                    long start1=System.currentTimeMillis();
                    String imgUrl = "https://api.lolicon.app/setu/v2/";
                    String tag = "";
                    int length = mesJson.length();
                    //获取最后两个关键词
                    String lastName = mesJson.substring(length - 2, length);
                    //只有最后两个关键词为涩图才按匹配
                    if (lastName.equals("涩图") && length > 2) {
                        try {
                            tag = Urlconvert.urlconvert(mesJson.substring(0, length - 2));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        imgUrl = imgUrl + "?tag=" + tag;
                    }
                    System.out.println("图片地址:"+imgUrl);
                    //获取图片地址
                    String getImageUrl = URLUtils.getImageUrl(imgUrl);
                    System.out.println("取得的涩图地址为："+getImageUrl);
                    if (getImageUrl.equals("error")){
                        event.getSubject().sendMessage("获取地址失败");
                    }else if (getImageUrl.equals("")) {
                        MessageChain chain = new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage()))
                                .append("没有此tag的涩图")
                                .build();
                        event.getSubject().sendMessage(chain);
                    }else {
                        long start2=System.currentTimeMillis();
                        InputStream is = null;
                        //获得输入流
                        is = URLUtils.accessImageUrl(getImageUrl);
                        Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), is);
                        try {
                            //记得关闭流
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        long end2=System.currentTimeMillis();
                        System.out.println("获取图片的时间为："+(end2-start2));
                        long start3=System.currentTimeMillis();
                        event.getSubject().sendMessage(image);
                        long end3=System.currentTimeMillis();
                        System.out.println("发送图片的时间为："+(end3-start3));
                        System.out.println("总时间为："+(end3-start1));
                    }

                }
            }
        });

        //检测入群
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinRequestEvent.class, event -> {
            event.accept();  //机器人自动审批

            long fromId = event.getFromId();//获取新人id
            long groupId = event.getGroupId();//获取群id
            if (groupId == 881426228) {
                // 发送At消息
                At at = new At(fromId);
                // 自定义图片
                // 发送图片需要先将图片上传到服务器
                File f = new File("C:\\Users\\无限缤纷\\Desktop\\imageTest\\44.png");
                Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.component1().getGroup(groupId), f);
                //自定义欢迎信息
                String mes = "    欢迎";
                //封装
                MessageChain messageChain = new MessageChainBuilder().append(at).append(mes).append(image).build();
                //发送
                Bot.getInstances().get(0).getGroup(groupId).sendMessage(messageChain);
            }
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(MessageRecallEvent.GroupRecall.class, event -> {

            long groupId = event.getGroup().getId();
            if (groupId==881426228){
                int[] messageIds = event.getMessageIds();
                System.out.println("撤回时间为:"+event.getMessageTime());
                //发送
            }
        });
    }



    public static String[] getFileName(String path) {
        File f = new File(path);//获取路径
        if (!f.exists()) {
            System.out.println(path + " not exists");//不存在就输出
        }
        File fa[] = f.listFiles();//用数组接收
        String fileName[] = new String[fa.length];
        for (int i = 0; i < fa.length; i++) {//循环遍历
            File fs = fa[i];//获取数组中的第i个
            fileName[i] = fs.getName();
        }
        return fileName;
    }

}