package cn.weegoo.config;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import cn.weegoo.sys.service.dto.DataRuleDTO;
import cn.weegoo.sys.utils.UserUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义数据权限
 *
 * @Date 2021-05-7
 */
public class DataPermissionHandlerImpl implements DataPermissionHandler {

    private final Logger logger = LoggerFactory.getLogger ( DataPermissionHandlerImpl.class );

    /**
     * 数据范围过滤
     *
     * @param where 当前过滤的实体类
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        if ( UserUtils.getCurrentUserDTO ( ) == null || StrUtil.isBlank ( UserUtils.getCurrentUserDTO ( ).getId ( ) ) ) {
            return where;
        }
        List <DataRuleDTO> dataRuleList = UserUtils.getDataRuleList ( );


        // 不存在数据规则，则不过滤数据
        if ( UserUtils.getCurrentUserDTO ( ).isAdmin ( ) || dataRuleList.size ( ) == 0 ) {
            return where;
        }

        // 数据范围
        Expression expression = null;
        for (DataRuleDTO dataRule : dataRuleList) {
            if ( mappedStatementId.equals ( dataRule.getClassName ( ) ) ) {
                Expression condExpression;
                try {
                    if ( dataRule.getValue ( ) != null && dataRule.getValue ( ).startsWith ( "@currentUser" ) ) {
                        BeanPath resolver = new BeanPath ( StrUtil.subAfter ( dataRule.getValue ( ), ".", false ) );
                        Object result = resolver.get ( UserUtils.getCurrentUserDTO ( ) );
                        dataRule.setValue ( result.toString ( ) );
                    }
                    if ( dataRule.getSqlSegment ( ) != null ) {
                        String sql = dataRule.getSqlSegment ( );

                        String[] objStrs = {"@currentUser.id", "@currentUser.companyDTO.id", "@currentUser.officeDTO.id", "@currentUser.companyDTO.parentIds", "@currentUser.officeDTO.parentIds"};
                        for (int i = 0; i < objStrs.length; i++) {
                            String objStr = objStrs[i];
                            if ( sql.indexOf ( objStr ) >= 0 ) {
                                BeanPath resolver = new BeanPath ( StrUtil.subAfter ( objStr, ".", false ) );
                                Object result = resolver.get ( UserUtils.getCurrentUserDTO ( ) );
                                sql = sql.replace ( objStr, "'" + result.toString ( ) + "'" );
                            }
                        }
                        dataRule.setSqlSegment ( sql );
                    }
                    condExpression = CCJSqlParserUtil.parseCondExpression ( dataRule.getDataScopeSql ( ) );
                    expression = ObjectUtils.isNotEmpty ( expression ) ? new OrExpression ( expression, condExpression ) : condExpression;
                } catch (JSQLParserException e) {
                    e.printStackTrace ( );
                    logger.error ( "{}", e );
                }
            }

        }
        if ( ObjectUtils.isEmpty ( expression ) ) {
            return where;
        } else {
            return ObjectUtils.isNotEmpty ( where ) ? new AndExpression ( where, new Parenthesis ( expression ) ) : expression;
        }
    }


}
