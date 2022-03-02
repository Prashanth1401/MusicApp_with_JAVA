import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class JukeBox {
    public void display()  {

        try {

            System.out.println("______________________\n");
            System.out.println("|\uD83D\uDE4F\uD83D\uDE4F WELCOME TO THE JUKEBOX \uD83D\uDE4F\uD83D\uDE4F|");
            System.out.println("| \uD83D\uDE4F\uD83D\uDE4FTHE MUSICAL WORLD\uD83D\uDE4F\uD83D\uDE4F|");
            System.out.println("|_____________________|");


            Scanner scanObj=new Scanner(System.in);
            Connection conn = Connect.getConnection();
            System.out.println();
            String username="";
            Users user=new Users();
            int roweff=0;
            while(true){
                System.out.println("1.Sign Up\t2.LogIN");

                int choice=scanObj.nextInt();
                if(choice==1) {

                    roweff= user.createUser(conn);
                    if(roweff>0)
                        System.out.println("Sucessfully Registered.");

                }
                else if(choice==2) {
                    Scanner s=new Scanner(System.in);
                    System.out.print("\n➡️➡Enter your username: ");
                    username=s.next();
                    System.out.print("\n➡️➡Enter your password: ");
                    String password=s.next();

                    boolean validation=user.checkUsername(conn, username, password);
                    if(validation) {
                        JukeBox menu=new JukeBox();
                        menu.Mainmenu(username);
                    }
                    else {
                        System.out.println("\n\nIncorrect username or password.Please enter the proper Details");
                    }
                    break;
                }
                else {
                    System.out.print("\nInvalid choice. Please select from 1 or 2.");
                    continue;
                }
            }



            System.out.print("\n\t\tTHANK YOU !Visit Again :)");

        } catch (Exception e) {

        }
    }
    public void Mainmenu(String username) {
        int n=0;

        try {
            Connection conn = Connect.getConnection();
            Song song=new Song();
            Podcast podcast=new Podcast();
            Playlist playlist=new Playlist();
            System.out.println();
            while(true){
                System.out.println("\nEnter Choice:\n1.Display Songs\t\t2.Search Song\t3.Display Podcast\t4.Search Podcast \t5.Create Playlist\t6.Display Playlist\t7.Exit");
                int choice=new Scanner(System.in).nextInt();
                switch(choice) {
                    case 1:
                        song.displaySongs(conn);
                        song.playSongs();
                        break;
                    case 2:
                        List<Song> songList=new ArrayList<Song>();
                        songList=song.storeSongsInArrayList(conn);
                        song.searchSongs(songList);
                        break;
                    case 3:
                        podcast.displayPodcast(conn);
                        break;
                    case 4:
                        podcast.searchPodcast(conn);
                        break;
                    case 5:
                        playlist.createUserPlaylist(conn, username);
                        break;
                    case 6:
                        playlist.viewSongsInPlaylist(conn, username);
                        break;

                    default:n=7;break;

                }
                if(n>6)break;
            }

        }
        catch(Exception e) {
            e.getMessage();
        }

    }

    public void songMenu() {
        try {
            Connection conn = Connect.getConnection();
            Song song=new Song();
            Podcast podcast=new Podcast();
            System.out.println();
            while(true) {
                System.out.println("\nEnter Your Choice:1. Display Songs\t2. Search Songs\t\t3.Display Podcast\t 4.Search Podcast");
                int choice=new Scanner(System.in).nextInt();
                switch(choice) {
                    case 1:
                        song.displaySongs(conn);
                        song.playSongs();
                        break;
                    case 2:
                        List<Song> songList=new ArrayList<Song>();
                        songList=song.storeSongsInArrayList(conn);
                        song.searchSongs(songList);
                        break;
                    case 3:
                        podcast.displayPodcast(conn);
                        break;
                    case 4:
                        podcast.searchPodcast(conn);
                        break;



                }
            }
        }
        catch(Exception e) {
            e.getMessage();
        }
    }

}
