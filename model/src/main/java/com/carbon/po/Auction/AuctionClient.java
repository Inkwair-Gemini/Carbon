

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
//商品的竞价人员表
public class AuctionClient implements Serializable {
    private String auctionQuotaId; //单向竞价商品ID
    private String clientId; //成员
}
