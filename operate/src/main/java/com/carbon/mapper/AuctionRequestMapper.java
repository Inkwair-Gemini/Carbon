package com.carbon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.input.Auction.AuctionRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuctionRequestMapper extends BaseMapper<AuctionRequest> {

}
