package it.medialogic.mockapi.service.impl;

import it.medialogic.mockapi.constant.CfErrorConstant;
import it.medialogic.mockapi.exception.CustomException;
import it.medialogic.mockapi.service.CfUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class CfUtilServiceImpl implements CfUtilService {
    @Override
    public boolean checkCf(String s) {

        log.info("*** Check cf length ***");
        if(s.length() != 16) {
            throw new CustomException(CfErrorConstant.wrongLength);
        }

        log.info("*** Check that the first six values are uppercase characters ***");
        if(s.chars().limit(6).noneMatch(c -> c >= 65 && c <= 90)) {
            throw new CustomException(CfErrorConstant.wrongNameSurname);
        }

        log.info("*** Check cf year birth ***");
        // value from 0 to 9 (ascii)
        if(!(s.charAt(6) >= 48 && s.charAt(6) <= 57) && !(s.charAt(7) >= 48 && s.charAt(7) <= 57)) {
            throw new CustomException(CfErrorConstant.wrongYearBirth);
        }

        log.info("*** Check cf month birth ***");
        // value from A to E (ascii)
        if(!(s.charAt(8) >= 65 && s.charAt(8) <= 69)) {
            // value H
            if(!(s.charAt(8)==72)) {
                // value L, M
                if(!(s.charAt(8) >= 76 && s.charAt(8) <= 77)) {
                    // value P
                    if(!(s.charAt(8) == 80)) {
                        // value from R to T
                        if(!(s.charAt(8) >= 82 && s.charAt(8) <= 84)) {
                            throw new CustomException(CfErrorConstant.wrongMonth);
                        }
                    }
                }
            }
        }

        log.info("*** Check male/female day birth ***");
        // male: from 1 to 31; female: from 41 to 71
        try {
            int genre = Integer.parseInt(s.substring(9,11));
            if(genre==0) {
                throw new CustomException(CfErrorConstant.wrongDay);
            }
            if(!(genre >= 1 && genre <= 31) && !(genre >= 41 && genre <= 71)) {
                throw new CustomException(CfErrorConstant.wrongDay);
            }
        }catch (NumberFormatException e) {
            throw new CustomException(CfErrorConstant.wrongDay);
        }

        log.info("*** Check city ***"); // -> need a db
        if(!(s.charAt(11) >= 65 && s.charAt(11) <= 90)) {
            throw new CustomException(CfErrorConstant.wrongCity);
        }

        try {
            Integer.parseInt(s.substring(12, 15));
        }catch (NumberFormatException e) {
            throw new CustomException(CfErrorConstant.wrongCity);
        }

        log.info("*** Check last character control ***");
        if(!(s.charAt(s.length()-1) >= 65 && s.charAt(s.length()-1) <= 90)) {
            throw new CustomException(CfErrorConstant.wrongControlCharacter);
        }

        return true;
    }

    @Override
    public String getBirth(String s) {
        String m = null;
        String date = null;
        String y, d;
        // check if cf is valid
        log.info("*** Check if the fiscal code is valid ***");
        if(checkCf(s)) {
            log.info("*** Getting the year ***");
            // year
            y = s.substring(6,8);

            log.info("*** Getting the month ***");
            // month
            switch (s.substring(8,9)) {
                case "A" -> m = "01";
                case "B" -> m = "02";
                case "C" -> m = "03";
                case "D" -> m = "04";
                case "E" -> m = "05";
                case "H" -> m = "06";
                case "L" -> m = "07";
                case "M" -> m = "08";
                case "P" -> m = "09";
                case "R" -> m = "10";
                case "S" -> m = "11";
                case "T" -> m = "12";
            }

            log.info("*** Getting the day ***");
            // day (man/female)
            if(Integer.parseInt(s.substring(9,11)) <= 31) {
                d = s.substring(9,11);
            }
            else {
                d = String.valueOf(Integer.parseInt(s.substring(9,11))-40);
            }
            date = d + "-" + m + "-" + y;

            // check valid data
            log.info("*** Check if is a valid day ***");
            try {
                new SimpleDateFormat("dd-MM-yy").parse(date);
            }catch (ParseException e) {
                throw new CustomException(CfErrorConstant.wrongDate);
            }

        }
        return date;
    }

    @Override
    public int getAge(String s) throws CustomException {
        try {
            log.info("*** Getting the date of birth ***");
            Date birth = new SimpleDateFormat("dd-MM-yy").parse(getBirth(s));

            log.info("*** Getting the date of today ***");
            Date today = new Date();
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

            log.info("*** Calculate the age ***");
            return (Integer.parseInt(formatter.format(today))-Integer.parseInt(formatter.format(birth)))/ 10000;
        }catch (ParseException | CustomException e) {
            throw new CustomException(CfErrorConstant.wrongDate);
        }
    }

    // last char unavailable because of homonyms
    @Override
    public char getLastChar(String s) {

        int sum = 0;

        log.info("*** Sum the even position ***");
        // check for even position
        for(int i = 0; i < s.length()-1; i++) {
            if((i+1)%2==0) {
                if(s.charAt(i) >= 65 && s.charAt(i) <= 90) {
                    sum+=s.charAt(i)-65;
                }
                else if(s.charAt(i) >= 48 && s.charAt(i) <= 57) {
                    sum+=s.charAt(i)-48;
                }
            }
        }

        log.info("*** Sum the odd position ***");
        // check for odd position
        for(int i = 0; i < s.length(); i++) {
            if((i+1)%2!=0) {
                switch (s.charAt(i)) {
                    case 65 -> sum++;
                    case 67, 50 -> sum += 5;
                    case 68, 51 -> sum += 7;
                    case 69, 52 -> sum += 9;
                    case 70, 53 -> sum += 13;
                    case 71, 54 -> sum += 15;
                    case 72, 55 -> sum += 17;
                    case 73, 56 -> sum += 19;
                    case 74, 57 -> sum += 21;
                    case 75 -> sum += 2;
                    case 76 -> sum += 4;
                    case 77 -> sum += 18;
                    case 78 -> sum += 20;
                    case 79 -> sum += 11;
                    case 80 -> sum += 3;
                    case 81 -> sum += 6;
                    case 82 -> sum += 8;
                    case 83 -> sum += 12;
                    case 84 -> sum += 14;
                    case 85 -> sum += 16;
                    case 86 -> sum += 10;
                    case 87 -> sum += 22;
                    case 88 -> sum += 25;
                    case 89 -> sum += 24;
                    case 90 -> sum += 23;
                    case 48 -> sum += 1;
                }
            }
        }

        log.info("*** Remainder of ((sum/26) up to to digit) mod 26  ***");
        // take the remainder
        String d = String.valueOf(Double.parseDouble(String.valueOf(sum))/Double.parseDouble(String.valueOf(26)));

        // if remainder have 1 digit; 2.5 for example
        if(d.substring(d.indexOf(".")).length()<=2) {
            String r = d.substring(d.indexOf(".")+1);

            // first digit remainder mod 26
            return (char) ((char) ((Integer.parseInt(r)%26)+1)+65);
        }

        // if remainder have 2 or more digit; 2.43958659 for example; it will take the first 2
        String r = d.substring(d.indexOf(".")+1, d.indexOf(".")+3);

        // first 2 digit remainder mod 26
        return (char) ((char) ((Integer.parseInt(r)%26)+1)+65);
    }

}
