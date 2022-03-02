import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Playlist {
    String PlaylistId;
    String SongID;
    String PodcastId;

    public void createUserPlaylist(Connection conn, String username) {
        try {
            String playlistId="";
            //Creating a new statement obj of class Statement and assigning it to connection statement//functioncall
            Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs=statement.executeQuery("select playlistID from UserPlaylist order by playlistID");
            while(rs.next()) {
                playlistId=rs.getString(1);
            }
            String newPlay="PLAY"+Integer.toString(Integer.parseInt(playlistId.substring(4))+1);
            PreparedStatement psmt=conn.prepareStatement("insert into UserPlaylist values(?,?,?)");
            psmt.setString(1, newPlay);
            System.out.print("\nEnter Name: ");
            String playlistName=new Scanner(System.in).next();
            psmt.setString(2, playlistName);
            psmt.setString(3, username);
            int row=psmt.executeUpdate();
            psmt.close();
            if(row>0) {
                System.out.print("\nPlaylist successfully created.\n");
                insertSongsIntoPlayList(newPlay,conn);
            }
            else {
                System.out.print("\nError");
            }
        }
        catch(Exception e) {
            System.out.print("Falied creating Playlist");
        }
    }
    public void insertSongsIntoPlayList(String playlistID, Connection conn) {
        Scanner scanObj=new Scanner(System.in);
        try {
            while(true){
                System.out.println("\nAdd\t\t1. Song\t\t2. Podcast\t\t3. Exit");
                int choice=scanObj.nextInt();
                if(choice==1) {
                    PreparedStatement psmt=conn.prepareStatement("Insert into Playlist (PlayListId,SongId,PodId) values(?,?,?)");
                    psmt.setString(1, playlistID);
                    System.out.print("\nSongID: ");
                    String songID=scanObj.next();
                    psmt.setString(2, songID);
                    psmt.setString(3, null);
                    int row=psmt.executeUpdate();
                    if(row>0) {
                        System.out.print("\nSong added.");
                    }
                    else {
                        System.out.print("\n\nUnable to add songs at this moment");
                    }
                }
                else if(choice==2) {
                    System.out.print("\nPodcast ID: ");
                    String podcastID=scanObj.next();

                    Statement statementObj=conn.createStatement();
                    ResultSet rs=statementObj.executeQuery("select podId from Podcast where podId='"+podcastID+"'");
                    int row = 0;
                    while(rs.next()) {


                        PreparedStatement psmt=conn.prepareStatement("Insert into Playlist (PlayListId,SongId,PodId) values(?,?,?)");
                        psmt.setString(1, playlistID);
                        psmt.setString(2, rs.getString(1));
                        psmt.setString(3, podcastID);
                        row=psmt.executeUpdate();
                    }
                    if(row>0) {
                        System.out.print("\n\nPodcast added.");
                    }
                    else {
                        System.out.print("\n\nUnable to add Podcast.Try Again");
                    }
                }
                else if(choice==3) {
                    break;
                }
                else {
                    System.out.print("\nNot Added Add Again\n");
                }
            }
        }
        catch(Exception e) {
            System.out.print("\nInvalid song code");
        }
    }

    public List<Song> viewSongsInPlaylist(Connection conn, String username) {
        ArrayList<Song> songList=new ArrayList<Song>();
        try {
            Statement statementObj=conn.createStatement();
            ResultSet rs=statementObj.executeQuery("select playlistID,playlistname from UserPlaylist where username='"+username+"'");
            int count=0;
            while(rs.next()) {
                System.out.print("\n Playlist ID= "+rs.getString(1)+", Name= "+rs.getString(2));
                count++;
            }
            if(count!=0) {
                System.out.print("\nEnter Playlist ID: ");
                String playlistId=new Scanner(System.in).next();
                ResultSet rs2=statementObj.executeQuery("select * from Song where songID in(select SongId from Playlist  where PlayListId  in (select playlistID from UserPlaylist where username='"+username+"' AND playlistID='"+playlistId+"'))");


                while(rs2.next()) {
                    songList.add(new Song(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6)));
                }
                System.out.println("\n_______________________________________");
                System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
                System.out.println("\n_______________________________________");
                songList.forEach(s->{
                    System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());
                });
                playSongsPlaylist(songList);
            }
            if(count==0) {
                System.out.print("\nNo Playlists Specified.Create One\n");
                createUserPlaylist(conn,username);
            }

        }
        catch(SQLException se) {
            System.out.print(se);
        }
        return songList;
    }
    public void playSongsPlaylist(List<Song> songList) {
        try {
            for(int i=0;i<songList.size();i++) {

                String url="C:\\Users\\Prashanth patel\\IdeaProjects\\JukeBox1\\src\\main\\resources\\"+songList.get(i).getSongID()+".wav";
                Clip clip=AudioSystem.getClip();
                File f=new File(url);

                AudioInputStream inputStream=AudioSystem.getAudioInputStream(f.getAbsoluteFile());

                clip.open(inputStream);
                clip.loop(0);
                clip.start();

                System.out.print("\n\t"+songList.get(i).getSongName()+"is Playing.....");
                System.out.print("\n\n\t1. Next");
                System.out.print("\t2. Prev");
                System.out.print("\t3. Restart");
                System.out.print("\t4. Stop");

                int choice=new Scanner(System.in).nextInt();
                while(true) {
                    if(clip.isActive()) {


                        if(choice==1) {
                            clip.close();
                            clip.stop();
                            break;
                        } else if(choice==2) {
                            clip.close();
                            clip.stop();
                            i=i-2;
                            break;
                        } else if(choice==3) {
                            clip.close();
                            clip.stop();
                            i=i-1;
                            break;
                        } else if(choice==4) {
                            break;
                        }

                    }
                    else {
                        clip.close();
                        clip.stop();
                        break;
                    }
                }
                if(choice==4) {
                    clip.close();
                    clip.stop();
                    System.out.print("\nReally Wanna Stop Playing?(Y/N) ");
                    String listen=new Scanner(System.in).next();
                    if(listen.equalsIgnoreCase("Y")) {
                        JukeBox menu=new JukeBox();
                        menu.songMenu();
                        break;
                    }
                    else if(listen.equalsIgnoreCase("N")) {
                        System.out.print("\nThank You See You Again .");
                        System.exit(0);
                    }

                }
            }
        }
        catch(Exception e) {
            System.out.print("\nJukebox stopped");
        }
    }
}
