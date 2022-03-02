import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import java.util.*;

public class PlayAudio {
    Clip clip;
    AudioInputStream audioInputStream;
    String filePath;
    Long currentFrame;
    String status;


    public PlayAudio() {

    }

    public void playAudio1(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.filePath = filePath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        clip = AudioSystem.getClip();

        clip.open(audioInputStream);


    }


    public void play() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        clip.start();
        status = "play";
    }

    public void pause() {
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (status.equals("play")) {
            System.out.println("Audio is already being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        play();
    }

    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        play();
    }

    public void stop() {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
    }
    public static void main(String[] args) {
        PlayAudio playAudio = new PlayAudio();
        int c = 0;
        try {

           // playAudio.playAudio1("C:\\Users\\prashanth patel\\IdeaProjects\\Jukebox1\\src\\main\\resources\\EvoEvo.WAV.wav");
           // playAudio.playAudio1("C:\\Users\\prashanth patel\\IdeaProjects\\Jukebox1\\src\\main\\resources\\Leharaayi.wav");
            playAudio.playAudio1("C:\\Users\\prashanth patel\\IdeaProjects\\Jukebox1\\src\\main\\resources\\manasa.WAV.wav");
           // playAudio.playAudio1("C:\\Users\\prashanth patel\\IdeaProjects\\Jukebox1\\src\\main\\resources\\Mounanga.WAV.wav");
           // playAudio.playAudio1("C:\\Users\\prashanth patel\\IdeaProjects\\Jukebox1\\src\\main\\resources\\Neechitram.WAV.wav");

            playAudio.play();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. pause");
                System.out.println("2. resume");
                System.out.println("3. restart");
                System.out.println("4. stop");

                if (c == 4) {
                    break;
                }

                if (scanner.hasNextInt()) {
                    c = scanner.nextInt();
                } else {
                    break;
                }
                switch (c) {
                    case 1:
                        playAudio.pause();
                        break;
                    case 2:
                        playAudio.resumeAudio();
                        break;
                    case 3:
                        playAudio.restart();
                        break;
                    case 4:
                        playAudio.stop();
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error with playing sound." + ex);
        }
    }
}