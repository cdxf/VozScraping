import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.github.lalyos.jfiglet.FigletFont;

import java.util.Random;

/**
 * Created by Snoob on 4/12/2016.
 */
public class AccountPoster {
    public String randomPassword() {
        Random random = new Random();
        String pass = "";
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 8; ++idx) {
            int randomInt = randomGenerator.nextInt(9);
            pass += String.valueOf(randomInt);
        }
        return pass;
    }

    public void changePassword(WebClient webClient, String currentPassword, String newPassword) throws Exception {
        HtmlPage page = webClient.getPage("https://vozforums.com/profile.php?do=editpassword");
        HtmlInput currentPasswordInput = null;
        try {
            currentPasswordInput = page.getElementByName("currentpassword");
        } catch (ElementNotFoundException e) {
            System.out.println(page.asText());
            System.exit(0);
        }
        HtmlInput newpasswordInput = page.getElementByName("newpassword");
        HtmlInput newpasswordconfirmInput = page.getElementByName("newpasswordconfirm");
        HtmlSubmitInput submitInput = (HtmlSubmitInput) page.getByXPath("//input[@value='Save Changes']").get(0);
        currentPasswordInput.setValueAttribute(currentPassword);
        newpasswordInput.setValueAttribute(newPassword);
        newpasswordconfirmInput.setValueAttribute(newPassword);
        submitInput.click();
    }

    public void changeAvatar(WebClient webClient, String avatar) throws Exception {
        HtmlPage page = webClient.getPage("https://vozforums.com/profile.php?do=editavatar");
        HtmlRadioButtonInput radio = (HtmlRadioButtonInput) page.getElementsByName("avatarid").get(1);
        HtmlTextInput url = page.getElementByName("avatarurl");
        HtmlSubmitInput submitInput = (HtmlSubmitInput) page.getByXPath("//input[@value='Save Changes']").get(0);
        radio.setChecked(true);
        url.setValueAttribute(avatar);
        submitInput.click();
    }

    public AccountPoster(String username, String currentPassword) throws Exception {
        String newPassword = randomPassword();
        String art = FigletFont.convertOneLine("Free  vozForums  Account") + "\n";
        art += FigletFont.convertOneLine(newPassword);
        System.out.println();
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setJavaScriptTimeout(500);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setUseInsecureSSL(true);
        //Login here
        HtmlPage page = webClient.getPage("https://vozforums.com");
        HtmlInput usernameInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_username']").get(0);
        HtmlInput passwordInput = (HtmlInput) page.getByXPath("//input[@name='vb_login_password']").get(0);
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Log in']").get(0);
        usernameInput.setValueAttribute(username);
        passwordInput.setValueAttribute(currentPassword);
        HtmlPage result = null;
        Page mpage = null;
        try {
            mpage = submit.click();
            result = (HtmlPage) mpage;
        } catch (ClassCastException e) {
            //Something wrong here
        }
        changePassword(webClient, currentPassword, newPassword);
        changeAvatar(webClient, "https://i.imgsafe.org/6ee51fa.jpg");
        page = webClient.getPage("https://vozforums.com/newthread.php?do=newthread&f=17");
        HtmlInput title = page.getElementByName("subject");
        title.setValueAttribute(RandomTitle.get());
        HtmlTextArea contentTA = (HtmlTextArea) page.getByXPath("//textarea").get(0);
        String content = "[CODE][B]" + art + "[/B][/CODE]\n";
        content +=
                "[I][COLOR=\"#4283C3\"]" +
                        "Tặng tài khoản vozForums cho các bạn, password là số bên trên.\n"+
                        "Các bạn vui lòng Edit hoặc Reply lại Thread sau khi đổi password để người khác không tốn công thử.\n" +
                        "Thread tạo tự động bởi Auto Giveaway Bot.\n" +
                        "glhf :D " +
                        "[/COLOR][/I]";
        contentTA.setText(content);
        HtmlSubmitInput submitBtn = (HtmlSubmitInput) page.getElementById("vB_Editor_001_save");
        HtmlPage a = submitBtn.click();
        System.out.println(a.asText());
    }
}
