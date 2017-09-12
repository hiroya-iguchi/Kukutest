package jp.alhinc.iguchi_hiroya.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CalculateTest {
      public static void main(String[] args){
    	 //1.支店定義ファイル読み込み
    	//コードと支店のHashMapを作成
 		  HashMap<String,String> branchNameMap = new HashMap<String, String>();
 		 Map<String,Long> branchSaleMap = new HashMap<String,Long>();
 		 HashMap<String,String> commodityNameMap = new HashMap<String, String>();
 		 Map<String,Long> commoditySaleMap = new HashMap<String, Long>();

    	  try{
    		  //指定ディレクトリからファイルを開く
    		  File file1 = new File(args[0]+"\\branch.lst");
    		  //支店定義ファイルの存在判別、フォーマット判別

    		  if(args.length != 1){
    			  System.out.println("予期せぬエラーが発生しました");
    			  return;
    		  }
    		  else if(!file1.exists()){
    			  System.out.println("支店定義ファイルが存在しません");
    			  return;
    		  }
    		  //1行ずつデータを読み込む
    		  FileReader fr1 = new FileReader(file1);
    		  BufferedReader br = new BufferedReader(fr1);
    		  String s1 = br.readLine();//ここではs1に1行目のデータが代入される



    		  //読み込んだ行がnullじゃない限り繰り返し
    		  while(s1 != null ){
    			  String[] branchFile = s1.split(",");//s1を","で分割したものを格納する配列を作成
    			  //formatの確認
    			  if(!branchFile[0].matches("^[0-9]*$")||branchFile[0].length() != 3 || branchFile.length != 2){
    				  System.out.println("支店定義ファイルのフォーマットが不正です");
    				  br.close();
    				  return;
    			  }
    			  branchNameMap.put(branchFile[0], branchFile[1]);
    			  s1 = br.readLine();//ここでs1が新たな行の読み込みデータに更新される(2行目、3行目、、、)
    			  branchSaleMap.put(branchFile[0], (long)0);

    		  }
    		  System.out.println(branchNameMap);
    		  System.out.println(branchSaleMap);
    		  br.close();//ストリームを閉じる


    	  }catch(IOException e){
    		  e.printStackTrace();
    		  return;
    	  }
    	  //2.商品定義ファイル読み込み
    	  try{
    		  //指定ディレクトリからファイルを開く
    		  File file2 = new File(args[0]+"\\commodity.lst");
    		  //支店定義ファイルの存在判別
    		  if(args.length != 1){
    			  System.out.println("予期せぬエラーが発生しました");
    			  return;
    		  }
    		  else if(!file2.exists()){
    			  System.out.println("商品定義ファイルが存在しません");
    			  return;
    		  }
    		  //1行ずつデータを読み込む
    		  FileReader fr2 = new FileReader(file2);
    		  BufferedReader br1 = new BufferedReader(fr2);
    		  String s2 = br1.readLine();
    		  //商品コードと商品名のHashMapを作成


    		  //読み込んだ行がnullじゃない限り繰り返し
    		  while(s2 != null ){
    			  String[] commodityFile = s2.split(",");//s2を","で分割したものを配列へ。
    			  //ファイルフォーマット判別
    			  if(!commodityFile[0].matches("^[0-9a-zA-Z]*$")||commodityFile[0].length() != 8 || commodityFile.length != 2){
    				  System.out.println("商品定義ファイルのフォーマットが不正です");
    				  br1.close();
    				  return;
    			  }
    			  commodityNameMap.put(commodityFile[0], commodityFile[1]);
    			  s2 = br1.readLine();
    			  commoditySaleMap.put(commodityFile[0], (long)0);

    		  }
    		  System.out.println(commodityNameMap);
    		  System.out.println(commoditySaleMap);
    		  br1.close();//ストリームを閉じる


    	  }catch(IOException e1){
    		  e1.printStackTrace();
    		  return;
    	  }
    	  //3.集計

          //3-1 連番ファイルの検索
    	  FilenameFilter filter = new FilenameFilter() {

  			public boolean accept(File file, String str){

  				// 拡張子を指定する
  				if (str.endsWith("rcd")){//ファイルの最後がrcdで終わるかどうか判別
  					return true;
  				}else{
  					return false;
  				}
  			}
  		};

  		//"rcd"が含まれるフィルタを作成する
  		File[] files = new File(args[0]).listFiles(filter);//rcdを持つものだけをファイル型の配列filesに格納する。
  		//結果を出力する

  		ArrayList<Integer> sequenceNumber = new ArrayList<Integer>();

  		String nameSeq;
  		for(int i=0; i<files.length; ++i){
  			System.out.println(files[i]);//配列の要素数より小さい場合は出力（チェック）
  			nameSeq = files[i].getName();//ファイル型配列の１つめの要素をString型へ。
  			System.out.println(nameSeq);//ここまでOKかチェック

  			String[] nameSeqs = nameSeq.split("\\.");//String型配列に8桁番号と拡張子を分割して格納

  			int j = Integer.parseInt(nameSeqs[0]);//String型8桁番号をint型に変更。
  			sequenceNumber.add(j);//int型のアレイリストに番号を格納していく(00000001,00000002)
  			if(i > 0){
  				if(sequenceNumber.get(i) != sequenceNumber.get(i-1)+1){//該当要素と1つ前の要素の連番を比較
  	  				System.out.println("売上ファイル名が連番になっていません");
  	  				return;
  			    }

  			}
  			    if(!files[i].isFile()){//ファイルかディレクトリかの判別
  				System.out.println("ディレクトリが含まれています");
  				return;
  			    }

  		}String fileName;
  		ArrayList<String> fileNames = new ArrayList<String>();//String型のArrayListを作成
  		//filesの個数分くりかえし
  		for(int i=0; i< files.length; i++){//配列filesの要素数より小さい場合の処理↓
  		    fileName = files[i].getName();//File型からString型への変換
  		    //桁数が12、かつ数字で始まりdで終わる場合を検討する
  			if(fileName.length()==12 && fileName.matches("^[0-9].*d$")){
  			fileNames.add(fileName);//アレイリストへの追加
  			}
  		 }
  		System.out.println(fileNames);
  		//3-2　売り上げファイルの読み込みと合算
  		try{

  		  //売り上げファイルの読み込み
  		  for(int i = 0; i< fileNames.size(); i++){ //ArrayListの要素数より少ない場合=要素すべてに対して以下を実行
  			ArrayList<String> saleFiles = new ArrayList<String>();//String型のアレイリストを作成
  			  //指定ディレクトリからファイルを開く
    		  File file3 = new File(args[0]+ "\\"+ fileNames.get(i));
    		//1行ずつデータを読み込む
      		  FileReader fr3 = new FileReader(file3);
      		  BufferedReader br2 = new BufferedReader(fr3);
      		  String s3 = br2.readLine();//1行目が読み込まれる


      		  //↓支店コードと合計金額の処理
      		  while(s3 != null){//行の中身がある限り下を繰り返す
      			  saleFiles.add(s3);//ArrayListに要素を追加する
      			  s3 = br2.readLine();//下の行を読む


      		  }if(saleFiles.size() != 3){//売上ファイルの行数が3行以外かの判別
  				  System.out.println(fileNames.get(i)+"のフォーマットが不正です");
  				  return;
  			  }else if(branchSaleMap.get(saleFiles.get(0)) == null){//支店コードに対応する売上がない場合
    				System.out.println(fileNames.get(i)+"の支店コードが不正です");

    			}if(commoditySaleMap.get(saleFiles.get(1)) == null){
      				System.out.println(fileNames.get(i)+"の商品コードが不正です");

    			}
      		  else if(branchSaleMap.get(saleFiles.get(0)) != null ){//取り出した要素をmapに入れてみてvalueが存在するか

      			long y = new Long(saleFiles.get(2)).longValue();//String型金額を数値に変換
      			long z = new Long(branchSaleMap.get(saleFiles.get(0))).longValue();//map1aのvalueを数値に変換
      			long t = y + z;
      			String branchSum = String.valueOf(t);

      		   if(branchSum.length() > 10){
      				System.out.println("合計金額が10桁を超えました");
      				return;
      			}


      			branchSaleMap.put(saleFiles.get(0), t);

      		  //商品コードと合計金額の処理
      			long v = new Long(saleFiles.get(2)).longValue();//String型金額を数値に変換
      			long w = new Long(commoditySaleMap.get(saleFiles.get(1))).longValue();//map1aのvalueを数値に変換
      			long x = v + w;
      			String commoditySum = String.valueOf(x);


      			 if(commoditySum.length() > 10){
      				System.out.println("合計金額が10桁を超えました");
      				return;
      			}

      			commoditySaleMap.put(saleFiles.get(1), x);
      		  }
     		   br2.close();//ストリームを閉じる
  		  }System.out.println(branchSaleMap);
  		   System.out.println(commoditySaleMap);
  		  }catch(IOException e2){
    		  e2.printStackTrace();
    		  System.out.println("予期せぬエラーが発生しました");
    		  return;


  	      }
  		//ファイルへの出力
  		try{
  			//合計金額を降順にする
  			List<Map.Entry<String,Long>> entries =
  	              new ArrayList<Map.Entry<String,Long>>(branchSaleMap.entrySet());
  	        Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {

  	            @Override
  	            public int compare(
  	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
  	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
  	            }
  	        });

  	          File file = new File(args[0] + "\\branch.out");
    			FileWriter fw = new FileWriter(file);
    			BufferedWriter bw = new BufferedWriter(fw);


    			for (Entry<String,Long> s : entries) {//branchファイルへの出力
    	            bw.write(s.getKey()+","+branchNameMap.get(s.getKey())+","+s.getValue()+"\r\n");
                    System.out.println(s.getKey()+","+branchNameMap.get(s.getKey())+","+s.getValue()+"\r\n");
    	        }bw.close();


  		}catch(IOException e2){
    		  e2.printStackTrace();
    		  System.out.println("予期せぬエラーが発生しました");
    		  return;


    	      }

  		try{
  	//商品金額を降順にする
			List<Map.Entry<String,Long>> entries1 =
	              new ArrayList<Map.Entry<String,Long>>(commoditySaleMap.entrySet());
	        Collections.sort(entries1, new Comparator<Map.Entry<String,Long>>() {

	            @Override
	            public int compare(
	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
	            }
	        });
	        File file1 = new File(args[0] + "\\commodity.out");
 			FileWriter fw1 = new FileWriter(file1);
 			BufferedWriter bw1 = new BufferedWriter(fw1);

 			for (Entry<String,Long> s : entries1){//commodityファイルへの出力
	        	bw1.write(s.getKey()+","+commodityNameMap.get(s.getKey())+","+s.getValue()+"\r\n");
	        	System.out.println(s.getKey()+","+commodityNameMap.get(s.getKey())+","+s.getValue()+"\r\n");
	        }
	         bw1.close();
  		}
  		catch(IOException e3){
  		  e3.printStackTrace();
  		  return;


	      }


      }//←mainメソッドの終点
      }

