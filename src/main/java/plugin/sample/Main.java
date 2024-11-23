package plugin.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private int count;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    // BigInteger型の val を定義
    BigInteger val = new BigInteger (String.valueOf(count));
    // BigInteger側の val に対してnextProbablePrimeメソッドを使用
    System.out.println(val.nextProbablePrime()); // 1より大きい素数の２が出力されます。

    // val が素数であるかの判定 isProbablePrimeメソッドを使用
    if (val.isProbablePrime(10)) {
      System.out.println(val + " は素数です");

      System.out.println("素数の場合処理を実行する");

      // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
      Firework firework = world.spawn(player.getLocation(), Firework.class);

      // 花火オブジェクトが持つメタ情報を取得。
      FireworkMeta fireworkMeta = firework.getFireworkMeta();

      // メタ情報に対して設定を追加したり、値の上書きを行う。
      // 今回は青色で星型の花火を打ち上げる。
      fireworkMeta.addEffect(
          FireworkEffect.builder()
              .withColor(Color.RED)
              .withColor(Color.YELLOW)
              .with(Type.BALL_LARGE)
              .withFlicker()
              .build());
      fireworkMeta.setPower(1 + 3 * 2 - 5);

      // 追加した情報で再設定する。
      firework.setFireworkMeta(fireworkMeta);

      Path path = Path.of("firework.txt");
      Files.writeString(path, "たーまやー", StandardOpenOption.APPEND);
      player.sendMessage(Files.readString(path));
    }
    count++;
  }

  @EventHandler
  public  void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        .filter(item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
        .forEach(item -> item.setAmount(64));

    player.getInventory().setContents(itemStacks);
  }
}


