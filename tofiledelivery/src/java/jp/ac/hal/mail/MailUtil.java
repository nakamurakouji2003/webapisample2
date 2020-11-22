package jp.ac.hal.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * メールに関するユーティリティクラス.
 */
public class MailUtil {

	/**
	 * コンストラクタ.
	 */
	private MailUtil() {
	}

	/**
	 * メイン.
	 * @param args 引数
	 */
	public static void main(final String[] args) {

		MailDataset mailDataset = new MailDataset();
		mailDataset.fromName = "送信者表示名";
		mailDataset.from = "xxxxxx@xxxx.xx.xx";
		mailDataset.toArray = new String[] {"xxxxxx@xxxx.xx.xx"};
		mailDataset.ccArray = new String[] {"xxxxxx@xxxx.xx.xx"};
		mailDataset.host = "smtp.xx.xx.xx";
		mailDataset.port = 587;
		mailDataset.authUsername = "xxxxxxxx";
		mailDataset.authPassword = "xxxxxxxx";
		mailDataset.starttls = false;
		mailDataset.subject = "テストメールの件名";
		mailDataset.content = "テストメールの本文";

		boolean ret = MailUtil.send(mailDataset);
	}

	/**
	 * データセット.
	 */
	public static class MailDataset {
		/** 送信者の表示名. */
		public String fromName;
		/** 送信アドレス. */
		public String from;
		/** 宛先(TO). */
		public String[] toArray = new String[0];
		/** 宛先(CC). */
		public String[] ccArray = new String[0];
		/** 宛先(BCC). */
		public String[] bccArray = new String[0];
		/** 認証(ユーザー名). */
		public String authUsername;
		/** 認証(パスワード). */
		public String authPassword;
		/** ホスト名. */
		public String host;
		/** ポート. */
		public int port;
		/** 暗号化(TLS保護接続への接続). */
		public boolean starttls = false;
		/** 件名. */
		public String subject;
		/** 本文. */
		public String content;
	}

	/**
	 * メール送信.
	 * @param mailDataset データセット
	 * @return true:成功、false:失敗
	 */
	public static boolean send(final MailDataset mailDataset) {
		// ----------------------------------
		// 属性
		//  参考URL) https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
		// ----------------------------------
		Properties props = new Properties();
		// ホスト名
		props.put("mail.smtp.host", mailDataset.host);
		// ポート番号
		props.put("mail.smtp.port", String.valueOf(mailDataset.port));
		// ソケット接続タイムアウト値(ミリ秒)
		props.put("mail.smtp.connectiontimeout", "10000");
		// ソケット読み取りタイムアウト値(ミリ秒)
		props.put("mail.smtp.timeout", "10000");
		// 暗号化(TLS保護接続への接続)
		props.put("mail.smtp.starttls.enable", String.valueOf(mailDataset.starttls));

		// デバッグ情報出力有無
		props.put("mail.debug", "true");

		// 認証設定
		javax.mail.Authenticator auth = null;
		if (mailDataset.authUsername != null && mailDataset.authPassword != null) {
			// ユーザー認証
			props.put("mail.smtp.auth", "true");
			// ユーザー名、パスワード設定
			auth = new javax.mail.Authenticator() {
				@Override
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(mailDataset.authUsername, mailDataset.authPassword);
				}
			};
		}

		// セッション生成
		Session session = Session.getInstance(props, auth);


		boolean ret = false;

		try {
			MimeMessage message = new MimeMessage(session);
			// 送信者
			message.setFrom(new InternetAddress(mailDataset.from, mailDataset.fromName));
			// 宛先(TO)
			for (String to : mailDataset.toArray) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			}
			// 宛先(CC)
			for (String cc : mailDataset.ccArray) {
				message.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}
			// 宛先(BCC)
			for (String bcc : mailDataset.bccArray) {
				message.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
			}

			final String charset = "UTF-8";
			final String encoding = "base64";

			// 送信日
			message.setSentDate(new Date());
			// 件名
			message.setSubject(mailDataset.subject, charset);
			// 本文
			message.setText(mailDataset.content, charset);
			// ヘッダー
			message.setHeader("Content-Transfer-Encoding", encoding);

			// 送信
			Transport.send(message);

			ret = true;

		} catch (AuthenticationFailedException e) {
			// 認証失敗
			e.printStackTrace();

		} catch (Exception e) {
			// その他例外
			e.printStackTrace();
		}

		return ret;
	}

}