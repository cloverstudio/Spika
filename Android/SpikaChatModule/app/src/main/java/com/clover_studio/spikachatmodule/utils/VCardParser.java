package com.clover_studio.spikachatmodule.utils;

import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Email;
import ezvcard.property.Telephone;

/**
 * Created by ubuntu_ivo on 17.08.15..
 */
public class VCardParser {

    /**
     * get name from vCard string
     * @param vCardString
     * @return name
     */
    public static String getNameFromVCard(String vCardString){

        VCard vCard = Ezvcard.parse(vCardString).first();
        String name = vCard.getFormattedName().getValue();

        return name;
    }

    /**
     * get name, first phone and first email from vCard string
     * @param vCardString
     * @return string[]{name,phone,email}
     */
    public static String[] getNameAndFirstPhoneAndFirstEmail(String vCardString, String noNameString){

        VCard vCard = Ezvcard.parse(vCardString).first();
        String name = "";

        if(vCard.getFormattedName() == null){
            name = noNameString;
        }else{
            name = vCard.getFormattedName().getValue();
        }

        List<Telephone> phones = vCard.getTelephoneNumbers();
        String phone = null;
        if(phones.size() > 0){
            phone = phones.get(0).getText();
        }

        List<Email> emails = vCard.getEmails();
        String email = null;
        if(emails.size() > 0){
            email = emails.get(0).getValue();
        }

        String[] returnData = new String[3];
        returnData[Const.ContactData.NAME] = name;
        returnData[Const.ContactData.PHONE] = phone;
        returnData[Const.ContactData.EMAIL] = email;

        return returnData;
    }

}
