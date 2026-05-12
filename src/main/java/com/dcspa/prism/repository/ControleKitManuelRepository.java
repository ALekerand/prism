package com.dcspa.prism.repository;

import com.dcspa.prism.entity.ControleKitManuel;
import com.dcspa.prism.repositorybase.BaseRepository;
import java.util.List;

public interface ControleKitManuelRepository extends BaseRepository<ControleKitManuel, Integer> {
	List<ControleKitManuel> findByControle_Id(Integer idControle);
}
