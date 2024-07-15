package com.wy.test.web.authentication.kerberos;

import java.util.Date;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;

import com.wy.test.provider.authn.support.kerberos.KerberosToken;
import com.wy.test.util.DateUtils;
import com.wy.test.util.JsonUtils;

public class KerberosPrincipal {

	/**
	 * 
	 */
	public KerberosPrincipal() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String kerberosPrincipal="Administrator@CONNSEC.COM";
		kerberosPrincipal=kerberosPrincipal.substring(0, kerberosPrincipal.indexOf("@"));
		System.out.println(kerberosPrincipal);

		if (Pattern.matches("[0-9]+", "TWO_WEEK")){
			System.out.println("true");
		}else{
			System.out.println("false");
		}
		
		DateTime datetime=new DateTime(new Date(), ISOChronology.getInstanceUTC());
		System.out.println(DateUtils.toUtc(datetime));
		
		datetime=datetime.plus(10*1000);
		
		System.out.println(DateUtils.toUtc(datetime));
		String json="{\"fullPrincipal\":\"Administrator@CONNSEC.COM\",\"principal\":\"Administrator\",\"userDomain\":\"CONNSEC\",\"notOnOrAfter\":\"2014-01-18T07:10:16.624Z\"}";
		KerberosToken kerberosToken=new KerberosToken();
		kerberosToken=(KerberosToken)JsonUtils.stringToObject(json, kerberosToken);
		
		System.out.println(kerberosToken);
		
		System.out.println(DateUtils.toUtcDate(kerberosToken.getNotOnOrAfter()));
		
	}

}
