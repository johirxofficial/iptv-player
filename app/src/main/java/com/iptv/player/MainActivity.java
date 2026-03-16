package com.iptv.player;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.bumptech.glide.Glide;

import org.json.*;

import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

RecyclerView grid;
LinearLayout categoryContainer;

List<JSONObject> channels = new ArrayList<>();
List<JSONObject> filtered = new ArrayList<>();

@Override
protected void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

grid = findViewById(R.id.channelGrid);
categoryContainer = findViewById(R.id.categoryContainer);

grid.setLayoutManager(new GridLayoutManager(this,4));

loadChannels();
createCategories();
showChannels(channels);

}

void loadChannels(){

try{

InputStream is = getAssets().open("channels.json");
BufferedReader br = new BufferedReader(new InputStreamReader(is));

StringBuilder sb = new StringBuilder();
String line;

while((line = br.readLine()) != null){
sb.append(line);
}

JSONArray arr = new JSONArray(sb.toString());

for(int i=0;i<arr.length();i++){
channels.add(arr.getJSONObject(i));
}

}catch(Exception e){}

}

void createCategories(){

Set<String> set = new HashSet<>();

for(JSONObject c : channels){

try{
set.add(c.getString("category"));
}catch(Exception e){}

}

for(String cat : set){

TextView btn = new TextView(this);
btn.setText(cat);
btn.setTextColor(0xffffffff);
btn.setPadding(40,15,40,15);
btn.setBackgroundResource(R.drawable.bg_capsule);

btn.setOnClickListener(v -> {

filtered.clear();

for(JSONObject ch : channels){

try{

if(ch.getString("category").equals(cat)){
filtered.add(ch);
}

}catch(Exception e){}

}

showChannels(filtered);

});

categoryContainer.addView(btn);

}

}

void showChannels(List<JSONObject> list){

grid.setAdapter(new RecyclerView.Adapter<ChannelVH>() {

@Override
public ChannelVH onCreateViewHolder(ViewGroup parent,int viewType){

LinearLayout box = new LinearLayout(MainActivity.this);
box.setOrientation(LinearLayout.VERTICAL);
box.setPadding(10,10,10,10);

ImageView logo = new ImageView(MainActivity.this);
logo.setLayoutParams(new LinearLayout.LayoutParams(-1,160));

TextView name = new TextView(MainActivity.this);
name.setTextColor(0xffffffff);
name.setTextSize(12);

box.addView(logo);
box.addView(name);

return new ChannelVH(box,logo,name);

}

@Override
public void onBindViewHolder(ChannelVH holder,int position){

JSONObject ch = list.get(position);

try{

holder.name.setText(ch.getString("name"));

Glide.with(MainActivity.this)
.load(ch.getString("logo"))
.into(holder.logo);

}catch(Exception e){}

}

@Override
public int getItemCount(){
return list.size();
}

});

}

class ChannelVH extends RecyclerView.ViewHolder{

ImageView logo;
TextView name;

ChannelVH(View v,ImageView l,TextView n){
super(v);
logo=l;
name=n;
}

}

}
