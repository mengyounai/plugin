package org.example;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.*;
import org.example.utils.JsonUtil;
import org.example.utils.Urlconvert;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;


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

            if (Num == 2572158616L) {
                if (mesJson.contains("晚安")){
                    String eve= JsonUtil.getEvening();
                    event.getSender().sendMessage(eve);
                }
                event.getSender().sendMessage(mesJson);
            }

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
                    File imageFile = null;
                    int length = mesJson.length();
                    String imgUrl = "https://api.lolicon.app/setu/v2/";
                    String tag = "";
                    //获取最后两个关键词
                    String lastName = mesJson.substring(mesJson.length() - 2, mesJson.length());
                    //只有最后两个关键词为涩图才按匹配
                    if (lastName.equals("涩图") && length > 2) {
                        try {
                            tag = Urlconvert.urlconvert(mesJson.substring(0, mesJson.length() - 2));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        imgUrl = imgUrl + "?tag=" + tag;
                    }
                    long start = System.currentTimeMillis();
                    String getImageUrl = getImageUrl(imgUrl);
                    long end = System.currentTimeMillis();
                    System.out.println("获取路径所花的时间："+(end-start));
                    long start2 = System.currentTimeMillis();
                    //定义一个URL对象，就是你想下载的图片的URL地址
                    URL url = null;
                    InputStream is=null;
                    try {
                        url = new URL(getImageUrl);
                        //打开连接
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
                        conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
                        //设置请求方式为"GET"
                        conn.setRequestMethod("GET");
                        //超时响应时间为10秒
                        conn.setConnectTimeout(10 * 1000);
                        //通过输入流获取图片数据
                        is = conn.getInputStream();
                    } catch (MalformedURLException | ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    imageFile = imageFileSave(getImageUrl);
                    long end2 = System.currentTimeMillis();
                    System.out.println("获取输出流所花的时间："+(end2-start2));
                    long start3 = System.currentTimeMillis();
//                    Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), imageFile);
                    Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), is);
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    event.getSubject().sendMessage(image);
                    long end3 = System.currentTimeMillis();
                    System.out.println("发送图片所花的时间："+(end3-start3));
                    System.out.println("总时间："+(end3-start));
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
                net.mamoe.mirai.contact.Contact.uploadImage(event.component1().getGroup(groupId), f);
                //自定义欢迎信息
                String mes = "   欢迎";
                //封装
                MessageChain messageChain = new MessageChainBuilder().append(at).append(mes).append(image).build();
                //发送
                Bot.getInstances().get(0).getGroup(groupId).sendMessage(messageChain);
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


    public static String getImageUrl(String imageurl) {
        String s = "";
        try {
            //定义一个URL对象，就是你想下载的图片的URL地址
            URL url = new URL(imageurl);
            //读取为String
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s2 = reader.readLine();
//            while ((s2 = reader.readLine()) != null) {
//                System.out.println(s2);
//            }
            reader.close();

            //判断是否有图片链接
            if (s2.contains("https")) {
                int index = s2.lastIndexOf(":");
                int end = s2.lastIndexOf("\"");
                //获取链接
                s = s2.substring(index - 5, end);
            }
            //设置图片大小
            String before=s.substring(0,19);
            String after=s.substring(19);
            s= before+"/c/540x540_70"+after;
            JSONObject obj = new JSONObject();
            JSONArray jsonArray = obj.getJSONArray(s2);
            System.out.println("jsonArray:"+jsonArray);
        } catch (Exception e) {
            System.out.println("获取图片地址出错");
        }
        return s;
    }

}