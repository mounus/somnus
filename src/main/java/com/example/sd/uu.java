package com.example.sd;

import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class uu {
    public static void main(String[] args) {

        String aa = "[[1,2],[1,3]]";
        List<List> list = JSONObject.parseArray(aa, List.class);

      //  String bb = "[[2,1],[2,2],[3,2],[3,1],[3,3],[2,4],[4,2],[2,9],[2,4]]";
        String bb = "[[2,1],[3,2],[3,1]]";
        List<List> xxlist = JSONObject.parseArray(bb, List.class);

        List<List> storeList = new ArrayList<>();
        for (int i = 0; i < xxlist.size(); i++) {
            if (xxlist.get(i).size() == 1) {
                storeList.clear();
                List<Integer> breakList = new ArrayList<>();
                breakList.add(1);
                storeList.add(breakList);
                break;
            } else {

                if (xxlist.get(i).get(1).toString().equals("1")){
                    for (int j = 0; j <storeList.size() ; j++) {
                        if (storeList.get(j).get(0)==xxlist.get(i).get(0)){
                            storeList.remove(j);
                        }else {

                        }
                    }
                    List<Integer> ssList=new ArrayList<>();
                    ssList.add(Integer.valueOf(xxlist.get(i).get(0).toString()));
                    ssList.add(1);
                    storeList.add(ssList);

                }else {

                    List<Integer> sslist=new ArrayList<>();
                    for (int j = 0; j <storeList.size() ; j++) {
                        if (storeList.get(j).get(1).toString().equals("1")){
                            sslist.add(Integer.valueOf(storeList.get(j).get(0).toString()));
                        }else {

                        }

                    }
                    if (sslist.contains(xxlist.get(i).get(0))){

                    }else {
                        List<Integer> aaList=new ArrayList<>();
                        aaList.add(Integer.valueOf(xxlist.get(i).get(0).toString()));
                        aaList.add(Integer.valueOf(xxlist.get(i).get(1).toString()));
                        storeList.add(aaList);
                    }

                }


            }

        }

        System.out.println("storeList = " + storeList);
    }

}
